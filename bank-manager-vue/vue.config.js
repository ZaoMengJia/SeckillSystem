module.exports ={
    devServer:{
        open:true ,
        proxy:{
            "/back":{
                changeOrigin:true,
                target:'http://localhost:8001',
                pathRewrite:{
                    '^/back':'/'
                }
            }
        }
    }
}