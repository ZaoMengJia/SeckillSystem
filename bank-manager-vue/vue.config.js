module.exports ={
    devServer:{
        open:true ,
        proxy:{
            "/":{
                changeOrigin:true,
                target:'http://localhost:8811',
            }
        }
    }
}