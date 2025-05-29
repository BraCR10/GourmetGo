package gourmetgo.client

object AppConfig {

    //Flag to use mockups
    const val USE_MOCKUP = true
    const val API_BASE_URL = "http://0.0.0.0:3000/api/"
    //Flag to allow login
    const val ENABLE_LOGGING = true
    //Param to config a delay when using mockups in the requests
    const val MOCK_NETWORK_DELAY = 800L //mil seconds
}