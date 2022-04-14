module.exports ={
    devServer:{
        open:true ,
        proxy:{
            "/back":{
                changeOrigin:true,
                target:'http://localhost:8811',
                pathRewrite:{
                    '^/back':'/'
                }
            }
        }
    }
}