package com.pax.demoapp.ui.model;

/**
 * @author ligq
 * @date 2018/9/30
 */

public class WeatherResponse {

    /**
     * resultcode : 200
     * reason : successed!
     * result : {"sk":{"temp":"30","wind_direction":"东北风","wind_strength":"2级","humidity":"49%","time":"14:57"},"today":{"temperature":"20℃~30℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"持续无风向微风","week":"星期日","city":"深圳","date_y":"2018年09月30日","dressing_index":"热","dressing_advice":"天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。","uv_index":"中等","comfort_index":"","wash_index":"较适宜","travel_index":"适宜","exercise_index":"适宜","drying_index":""},"future":{"day_20180930":{"temperature":"20℃~30℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"持续无风向微风","week":"星期日","date":"20180930"},"day_20181001":{"temperature":"20℃~29℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"持续无风向微风","week":"星期一","date":"20181001"},"day_20181002":{"temperature":"21℃~28℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"持续无风向微风","week":"星期二","date":"20181002"},"day_20181003":{"temperature":"22℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期三","date":"20181003"},"day_20181004":{"temperature":"21℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期四","date":"20181004"},"day_20181005":{"temperature":"22℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期五","date":"20181005"},"day_20181006":{"temperature":"22℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期六","date":"20181006"}}}
     * error_code : 0
     */

    private String resultcode;
    private String reason;
    private ResultBean result;
    private int error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    @SuppressWarnings("unused")
    public static class ResultBean {
        /**
         * sk : {"temp":"30","wind_direction":"东北风","wind_strength":"2级","humidity":"49%","time":"14:57"}
         * today : {"temperature":"20℃~30℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"持续无风向微风","week":"星期日","city":"深圳","date_y":"2018年09月30日","dressing_index":"热","dressing_advice":"天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。","uv_index":"中等","comfort_index":"","wash_index":"较适宜","travel_index":"适宜","exercise_index":"适宜","drying_index":""}
         * future : {"day_20180930":{"temperature":"20℃~30℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"持续无风向微风","week":"星期日","date":"20180930"},"day_20181001":{"temperature":"20℃~29℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"持续无风向微风","week":"星期一","date":"20181001"},"day_20181002":{"temperature":"21℃~28℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"持续无风向微风","week":"星期二","date":"20181002"},"day_20181003":{"temperature":"22℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期三","date":"20181003"},"day_20181004":{"temperature":"21℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期四","date":"20181004"},"day_20181005":{"temperature":"22℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期五","date":"20181005"},"day_20181006":{"temperature":"22℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期六","date":"20181006"}}
         */

        private SkBean sk;
        private TodayBean today;
        private FutureBean future;

        public SkBean getSk() {
            return sk;
        }

        public void setSk(SkBean sk) {
            this.sk = sk;
        }

        public TodayBean getToday() {
            return today;
        }

        public void setToday(TodayBean today) {
            this.today = today;
        }

        public FutureBean getFuture() {
            return future;
        }

        public void setFuture(FutureBean future) {
            this.future = future;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "sk=" + sk.toString() +
                    ", today=" + today.toString() +
                    ", future=" + future.toString() +
                    '}';
        }

        @SuppressWarnings("unused")
        public static class SkBean {
            /**
             * temp : 30
             * wind_direction : 东北风
             * wind_strength : 2级
             * humidity : 49%
             * time : 14:57
             */

            private String temp;
            private String wind_direction;
            private String wind_strength;
            private String humidity;
            private String time;

            public String getTemp() {
                return temp;
            }

            public void setTemp(String temp) {
                this.temp = temp;
            }

            public String getWind_direction() {
                return wind_direction;
            }

            public void setWind_direction(String wind_direction) {
                this.wind_direction = wind_direction;
            }

            public String getWind_strength() {
                return wind_strength;
            }

            public void setWind_strength(String wind_strength) {
                this.wind_strength = wind_strength;
            }

            public String getHumidity() {
                return humidity;
            }

            public void setHumidity(String humidity) {
                this.humidity = humidity;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            @Override
            public String toString() {
                return "SkBean{" +
                        "temp='" + temp + '\'' +
                        ", wind_direction='" + wind_direction + '\'' +
                        ", wind_strength='" + wind_strength + '\'' +
                        ", humidity='" + humidity + '\'' +
                        ", time='" + time + '\'' +
                        '}';
            }
        }

        @SuppressWarnings("unused")
        public static class TodayBean {
            /**
             * temperature : 20℃~30℃
             * weather : 多云
             * weather_id : {"fa":"01","fb":"01"}
             * wind : 持续无风向微风
             * week : 星期日
             * city : 深圳
             * date_y : 2018年09月30日
             * dressing_index : 热
             * dressing_advice : 天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。
             * uv_index : 中等
             * comfort_index :
             * wash_index : 较适宜
             * travel_index : 适宜
             * exercise_index : 适宜
             * drying_index :
             */

            private String temperature;
            private String weather;
            private WeatherIdBean weather_id;
            private String wind;
            private String week;
            private String city;
            private String date_y;
            private String dressing_index;
            private String dressing_advice;
            private String uv_index;
            private String comfort_index;
            private String wash_index;
            private String travel_index;
            private String exercise_index;
            private String drying_index;

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public WeatherIdBean getWeather_id() {
                return weather_id;
            }

            public void setWeather_id(WeatherIdBean weather_id) {
                this.weather_id = weather_id;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDate_y() {
                return date_y;
            }

            public void setDate_y(String date_y) {
                this.date_y = date_y;
            }

            public String getDressing_index() {
                return dressing_index;
            }

            public void setDressing_index(String dressing_index) {
                this.dressing_index = dressing_index;
            }

            public String getDressing_advice() {
                return dressing_advice;
            }

            public void setDressing_advice(String dressing_advice) {
                this.dressing_advice = dressing_advice;
            }

            public String getUv_index() {
                return uv_index;
            }

            public void setUv_index(String uv_index) {
                this.uv_index = uv_index;
            }

            public String getComfort_index() {
                return comfort_index;
            }

            public void setComfort_index(String comfort_index) {
                this.comfort_index = comfort_index;
            }

            public String getWash_index() {
                return wash_index;
            }

            public void setWash_index(String wash_index) {
                this.wash_index = wash_index;
            }

            public String getTravel_index() {
                return travel_index;
            }

            public void setTravel_index(String travel_index) {
                this.travel_index = travel_index;
            }

            public String getExercise_index() {
                return exercise_index;
            }

            public void setExercise_index(String exercise_index) {
                this.exercise_index = exercise_index;
            }

            public String getDrying_index() {
                return drying_index;
            }

            public void setDrying_index(String drying_index) {
                this.drying_index = drying_index;
            }

            @Override
            public String toString() {
                return "TodayBean{" +
                        "temperature='" + temperature + '\'' +
                        ", weather='" + weather + '\'' +
                        ", weather_id=" + weather_id.toString() +
                        ", wind='" + wind + '\'' +
                        ", week='" + week + '\'' +
                        ", city='" + city + '\'' +
                        ", date_y='" + date_y + '\'' +
                        ", dressing_index='" + dressing_index + '\'' +
                        ", dressing_advice='" + dressing_advice + '\'' +
                        ", uv_index='" + uv_index + '\'' +
                        ", comfort_index='" + comfort_index + '\'' +
                        ", wash_index='" + wash_index + '\'' +
                        ", travel_index='" + travel_index + '\'' +
                        ", exercise_index='" + exercise_index + '\'' +
                        ", drying_index='" + drying_index + '\'' +
                        '}';
            }

            public static class WeatherIdBean {
                /**
                 * fa : 01
                 * fb : 01
                 */

                private String fa;
                private String fb;

                public String getFa() {
                    return fa;
                }

                public void setFa(String fa) {
                    this.fa = fa;
                }

                public String getFb() {
                    return fb;
                }

                public void setFb(String fb) {
                    this.fb = fb;
                }

                @Override
                public String toString() {
                    return "WeatherIdBean{" +
                            "fa='" + fa + '\'' +
                            ", fb='" + fb + '\'' +
                            '}';
                }
            }
        }

        @SuppressWarnings("unused")
        public static class FutureBean {
            /**
             * day_20180930 : {"temperature":"20℃~30℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"持续无风向微风","week":"星期日","date":"20180930"}
             * day_20181001 : {"temperature":"20℃~29℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"持续无风向微风","week":"星期一","date":"20181001"}
             * day_20181002 : {"temperature":"21℃~28℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"持续无风向微风","week":"星期二","date":"20181002"}
             * day_20181003 : {"temperature":"22℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期三","date":"20181003"}
             * day_20181004 : {"temperature":"21℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期四","date":"20181004"}
             * day_20181005 : {"temperature":"22℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期五","date":"20181005"}
             * day_20181006 : {"temperature":"22℃~29℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"持续无风向微风","week":"星期六","date":"20181006"}
             */

            private Day20180930Bean day_20180930;
            private Day20181001Bean day_20181001;
            private Day20181002Bean day_20181002;
            private Day20181003Bean day_20181003;
            private Day20181004Bean day_20181004;
            private Day20181005Bean day_20181005;
            private Day20181006Bean day_20181006;

            public Day20180930Bean getDay_20180930() {
                return day_20180930;
            }

            public void setDay_20180930(Day20180930Bean day_20180930) {
                this.day_20180930 = day_20180930;
            }

            public Day20181001Bean getDay_20181001() {
                return day_20181001;
            }

            public void setDay_20181001(Day20181001Bean day_20181001) {
                this.day_20181001 = day_20181001;
            }

            public Day20181002Bean getDay_20181002() {
                return day_20181002;
            }

            public void setDay_20181002(Day20181002Bean day_20181002) {
                this.day_20181002 = day_20181002;
            }

            public Day20181003Bean getDay_20181003() {
                return day_20181003;
            }

            public void setDay_20181003(Day20181003Bean day_20181003) {
                this.day_20181003 = day_20181003;
            }

            public Day20181004Bean getDay_20181004() {
                return day_20181004;
            }

            public void setDay_20181004(Day20181004Bean day_20181004) {
                this.day_20181004 = day_20181004;
            }

            public Day20181005Bean getDay_20181005() {
                return day_20181005;
            }

            public void setDay_20181005(Day20181005Bean day_20181005) {
                this.day_20181005 = day_20181005;
            }

            public Day20181006Bean getDay_20181006() {
                return day_20181006;
            }

            public void setDay_20181006(Day20181006Bean day_20181006) {
                this.day_20181006 = day_20181006;
            }

            public static class Day20180930Bean {
                /**
                 * temperature : 20℃~30℃
                 * weather : 多云
                 * weather_id : {"fa":"01","fb":"01"}
                 * wind : 持续无风向微风
                 * week : 星期日
                 * date : 20180930
                 */

                private String temperature;
                private String weather;
                private WeatherIdBeanX weather_id;
                private String wind;
                private String week;
                private String date;

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public static class WeatherIdBeanX {
                    /**
                     * fa : 01
                     * fb : 01
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20181001Bean {
                /**
                 * temperature : 20℃~29℃
                 * weather : 多云
                 * weather_id : {"fa":"01","fb":"01"}
                 * wind : 持续无风向微风
                 * week : 星期一
                 * date : 20181001
                 */

                private String temperature;
                private String weather;
                private WeatherIdBeanXX weather_id;
                private String wind;
                private String week;
                private String date;

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanXX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanXX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public static class WeatherIdBeanXX {
                    /**
                     * fa : 01
                     * fb : 01
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20181002Bean {
                /**
                 * temperature : 21℃~28℃
                 * weather : 多云
                 * weather_id : {"fa":"01","fb":"01"}
                 * wind : 持续无风向微风
                 * week : 星期二
                 * date : 20181002
                 */

                private String temperature;
                private String weather;
                private WeatherIdBeanXXX weather_id;
                private String wind;
                private String week;
                private String date;

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanXXX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanXXX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public static class WeatherIdBeanXXX {
                    /**
                     * fa : 01
                     * fb : 01
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20181003Bean {
                /**
                 * temperature : 22℃~29℃
                 * weather : 晴
                 * weather_id : {"fa":"00","fb":"00"}
                 * wind : 持续无风向微风
                 * week : 星期三
                 * date : 20181003
                 */

                private String temperature;
                private String weather;
                private WeatherIdBeanXXXX weather_id;
                private String wind;
                private String week;
                private String date;

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanXXXX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanXXXX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public static class WeatherIdBeanXXXX {
                    /**
                     * fa : 00
                     * fb : 00
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20181004Bean {
                /**
                 * temperature : 21℃~29℃
                 * weather : 晴
                 * weather_id : {"fa":"00","fb":"00"}
                 * wind : 持续无风向微风
                 * week : 星期四
                 * date : 20181004
                 */

                private String temperature;
                private String weather;
                private WeatherIdBeanXXXXX weather_id;
                private String wind;
                private String week;
                private String date;

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanXXXXX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanXXXXX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public static class WeatherIdBeanXXXXX {
                    /**
                     * fa : 00
                     * fb : 00
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20181005Bean {
                /**
                 * temperature : 22℃~29℃
                 * weather : 晴
                 * weather_id : {"fa":"00","fb":"00"}
                 * wind : 持续无风向微风
                 * week : 星期五
                 * date : 20181005
                 */

                private String temperature;
                private String weather;
                private WeatherIdBeanXXXXXX weather_id;
                private String wind;
                private String week;
                private String date;

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanXXXXXX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanXXXXXX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public static class WeatherIdBeanXXXXXX {
                    /**
                     * fa : 00
                     * fb : 00
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20181006Bean {
                /**
                 * temperature : 22℃~29℃
                 * weather : 晴
                 * weather_id : {"fa":"00","fb":"00"}
                 * wind : 持续无风向微风
                 * week : 星期六
                 * date : 20181006
                 */

                private String temperature;
                private String weather;
                private WeatherIdBeanXXXXXXX weather_id;
                private String wind;
                private String week;
                private String date;

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanXXXXXXX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanXXXXXXX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public static class WeatherIdBeanXXXXXXX {
                    /**
                     * fa : 00
                     * fb : 00
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }
        }
    }
}
