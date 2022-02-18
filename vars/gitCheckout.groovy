def call(Map stageParams)
{
checkout([
 $class: 'GitSCM',
 branches: [[name: '*/main']], extensions: [], 
userRemoteConfigs: [[credentialsId: 'github_login', url: 'https://github.com/gopika-18/Jenkins_exercise.git']]
])
}