package com.coder.kzxt.buildwork.entity;

/**
 * Created by pc on 2017/2/20.
 */

public class QuestionNumber {
    /**
     * data : {"choice":{"type":"choice","num":"1"},"determine":{"type":"determine","num":"1"},"essay":{"type":"essay","num":"1"},"fill":{"type":"fill","num":"1"},"single_choice":{"type":"single_choice","num":"1"}}
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1487558339.956 e:1487558340.187 tms=231ms
     * mem : 19.48 MB
     * server : wyzc
     * serverTime : 1487558340
     */

    private DataBean data;
    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getXhprof() {
        return xhprof;
    }

    public void setXhprof(String xhprof) {
        this.xhprof = xhprof;
    }

    public String getRunTm() {
        return runTm;
    }

    public void setRunTm(String runTm) {
        this.runTm = runTm;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public static class DataBean {
        /**
         * choice : {"type":"choice","num":"1"}
         * determine : {"type":"determine","num":"1"}
         * essay : {"type":"essay","num":"1"}
         * fill : {"type":"fill","num":"1"}
         * single_choice : {"type":"single_choice","num":"1"}
         */

        private ChoiceBean choice;
        private DetermineBean determine;
        private EssayBean essay;
        private FillBean fill;
        private SingleChoiceBean single_choice;

        public ChoiceBean getChoice() {
            return choice;
        }

        public void setChoice(ChoiceBean choice) {
            this.choice = choice;
        }

        public DetermineBean getDetermine() {
            return determine;
        }

        public void setDetermine(DetermineBean determine) {
            this.determine = determine;
        }

        public EssayBean getEssay() {
            return essay;
        }

        public void setEssay(EssayBean essay) {
            this.essay = essay;
        }

        public FillBean getFill() {
            return fill;
        }

        public void setFill(FillBean fill) {
            this.fill = fill;
        }

        public SingleChoiceBean getSingle_choice() {
            return single_choice;
        }

        public void setSingle_choice(SingleChoiceBean single_choice) {
            this.single_choice = single_choice;
        }

        public static class ChoiceBean {
            /**
             * type : choice
             * num : 1
             */

            private String type;
            private int num;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }

        public static class DetermineBean {
            /**
             * type : determine
             * num : 1
             */

            private String type;
            private int num;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }

        public static class EssayBean {
            /**
             * type : essay
             * num : 1
             */

            private String type;
            private int num;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }

        public static class FillBean {
            /**
             * type : fill
             * num : 1
             */

            private String type;
            private int num;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }

        public static class SingleChoiceBean {
            /**
             * type : single_choice
             * num : 1
             */

            private String type;
            private int num;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }
    }
}
