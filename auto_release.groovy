properties([
  parameters([
    [
      $class              : 'CascadeChoiceParameter',
      choiceType          : 'PT_CHECKBOX',
      name                : 'REPOSITORY',
      referencedParameters: 'Environment',
      script              : [
        $class: 'GroovyScript',
        script: [
          classpath: [], sandbox: true,
          script   : 'return ["Weather", "SpringBootStudy", "Repo3"]'
        ]
      ]
    ]
  ])
])

pipeline {
  agent any

  options {
    disableConcurrentBuilds()
  }


  stages {

    stage('Checkout Repositories') {
      steps {
        script {
          def repositories = params.REPOSITORY.tokenize(",") // 解析多选参数
          if (repositories.isEmpty()) {
            error "必须至少选择一个仓库"
          }

          repositories.each { repo ->
            println "正在启动: ${repo} GitHub action"
            sh "pwd"
            def workflowId = getWorkflowId("KennyPacky", repo)
            triggerWorkflow("KennyPacky", repo, workflowId)
          }
        }
      }
    }

    stage('Trigger Job') {
      steps {
        script {
          build job: 'NewJobs/Hello',
                wait: false,
                parameters: [
                  booleanParam(name: 'Happy', value: true)
                ]
        }
      }
    }
  }

  post {
    always {
      cleanWs() // Ensures all cloned repositories are deleted after the pipeline runs
    }
  }
}

def getWorkflowId(repoOwner, repoName) {
    println "Get workflow id"
    withCredentials([string(credentialsId: 'Github_Token', variable: 'GITHUB_TOKEN')]) {
        def response = sh(script: """
            curl -s -H \"Authorization: Bearer \$GITHUB_TOKEN\" \
                 -H \"Accept: application/vnd.github.v3+json\" \
                 https://api.github.com/repos/${repoOwner}/${repoName}/actions/workflows
        """, returnStdout: true).trim()

        def jsonResponse = new groovy.json.JsonSlurper().parseText(response)
        println jsonResponse

        if (jsonResponse.workflows && jsonResponse.workflows.size() > 0) {
            return jsonResponse.workflows[0].id
        } else {
            error("No workflows found for repository ${repoOwner}/${repoName}")
        }
    }
}

def triggerWorkflow(repoOwner, repoName, workflowId) {
    println "Triggering workflow"
    withCredentials([string(credentialsId: 'Github_Token', variable: 'GITHUB_TOKEN')]) {
        def payload = [
            ref: 'develop',
            inputs: [
                source_branch: 'develop',
                target_branch: 'master'
            ]
        ]

        sh(script: """
            curl -X POST \
                 -H \"Accept: application/vnd.github.v3+json\" \
                 -H \"Authorization: Bearer \$GITHUB_TOKEN\" \
                 https://api.github.com/repos/${repoOwner}/${repoName}/actions/workflows/${workflowId}/dispatches \
                 -d '${groovy.json.JsonOutput.toJson(payload)}'
        """)
    }
}
