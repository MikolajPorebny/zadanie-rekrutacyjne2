package com.zadanierekrutacyjne2.model.loadmodel

class Owner {
        var avatar_url: String? = null      //git avatar
        var links: Links? = null
        var login: String? = null           //git user
        var nickname: String? = null        //bit user
}

class Links {
    var avatar: Avatar? = null
}

class Avatar {
    var href: String? = null                //bit avatar
}