pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh "mvn clean install -DskipTests"
            }
        }
        stage('Test') {
            steps {
                echo 'Testing....   '
            }
        }
		stage('Deploy') {
		  steps {
			echo 'Started sending file....  DevServer '
			sshPublisher(
				publishers: [
					sshPublisherDesc(
					configName: 'DevServer', 
					transfers: [
						sshTransfer(
							remoteDirectory: 'deployment',
							cleanRemote: false,
							sourceFiles: '**/target/*.war',
							usePty: true,
							makeEmptyDirs: false,
							noDefaultExcludes: false, 
							patternSeparator: '[, ]+',
							flatten: false,
							execTimeout: 0,
							removePrefix: 'target'
							)
						], 
					usePromotionTimestamp: false, 
					useWorkspaceInPromotion: false, 
					verbose: true
					)
				]
			)
		  }
		}
    }
}