package com.utsab.transformerbattleapp.helpers

/**
 * For static datas and messages
 */
object Constants {


//region API URLS
    const val BASE_URL = "https://transformers-api.firebaseapp.com"
    const val TOKEN_URL = "/allspark"
    const val GET_TRANSFORMERS_URL = "/transformers"
    const val UPDATE_TRANSFORMERS_URL = "/transformers/{transformerId}"
    //endregion API URLS


    //Pref Constants
    const val USER_TOKEN = "user-token"

    //Messages for user
    const val NO_USER_TOKEN = "User Token not present"
    const val NO_INTERNET_CONNECTION = "Internet Connection not available"
    const val UNKNOWN_FAILURE = "Unknown failure occured. Please try again later."
    const val DELETED_MESSAGE = "Deleted Successfully"
    const val ALL_DESTROYED = "All Transformers were destroyed"
    const val UPDATED_SUCCESSFULLY = "Updated Successfully"
    const val CREATED_MESSAGE = "Transformer created successfully"

    //    Teams
    const val TEAM_DECEPTICON = "D"
    const val TEAM_AUTOBOT = "A"
    const val TEAM = "team"
    const val PREDAKING = "Predaking"
    const val OPTIMUS = "Optimus Prime"

    const val ID = "id"
    const val TRANSFORMER_MODEL = "transformer_model"
    const val TEAM_ICON = "team_icon"
    const val DRAW = "DRAW"
    const val NO_BATTLE = "not_enough_members"

}