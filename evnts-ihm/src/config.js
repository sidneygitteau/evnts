module.exports = global.config = {
    url:{
        root_api:'http://localhost:8080/',
        auth:{
            connexion:'auth/connexion',
            inscription:'auth/inscription',
            suppression:'auth/suppression',
            deconnexion:'auth/deconnexion'
        },
        salon:{
            creer:'/salon',
            get:'/salon/'
        }
    }
}