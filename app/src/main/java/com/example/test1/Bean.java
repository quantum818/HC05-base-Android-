package com.example.test1;

public class Bean {
    public String code;
    public String updateTime;
    public Now now;
}

    class Now {
        private String obsTime;

        private String temp;

        private String feelsLike;

        private String icon;

        private String text;

        private String wind360;

        private String windDir;

        private String windScale;

        private String windSpeed;

        private String humidity;

        private String precip;

        private String pressure;

        private String vis;

        private String cloud;

        private String dew;
        public void setObsTime(String obsTime){
            this.obsTime = obsTime;
        }
        public String getObsTime(){
            return this.obsTime;
        }
        public void setTemp(String temp){
            this.temp = temp;
        }
        public String getTemp(){
            return this.temp;
        }
        public void setFeelsLike(String feelsLike){
            this.feelsLike = feelsLike;
        }
        public String getFeelsLike(){
            return this.feelsLike;
        }
        public void setIcon(String icon){
            this.icon = icon;
        }
        public String getIcon(){
            return this.icon;
        }
        public void setText(String text){
            this.text = text;
        }
        public String getText(){
            return this.text;
        }
        public void setWind360(String wind360){
            this.wind360 = wind360;
        }
        public String getWind360(){
            return this.wind360;
        }
        public void setWindDir(String windDir){
            this.windDir = windDir;
        }
        public String getWindDir(){
            return this.windDir;
        }
        public void setWindScale(String windScale){
            this.windScale = windScale;
        }
        public String getWindScale(){
            return this.windScale;
        }
        public void setWindSpeed(String windSpeed){
            this.windSpeed = windSpeed;
        }
        public String getWindSpeed(){
            return this.windSpeed;
        }
        public void setHumidity(String humidity){
            this.humidity = humidity;
        }
        public String getHumidity(){
            return this.humidity;
        }
        public void setPrecip(String precip){
            this.precip = precip;
        }
        public String getPrecip(){
            return this.precip;
        }
        public void setPressure(String pressure){
            this.pressure = pressure;
        }
        public String getPressure(){
            return this.pressure;
        }
        public void setVis(String vis){
            this.vis = vis;
        }
        public String getVis(){
            return this.vis;
        }
        public void setCloud(String cloud){
            this.cloud = cloud;
        }
        public String getCloud(){
            return this.cloud;
        }
        public void setDew(String dew){
            this.dew = dew;
        }
        public String getDew(){
            return this.dew;
        }
        // gson解析类
    }
