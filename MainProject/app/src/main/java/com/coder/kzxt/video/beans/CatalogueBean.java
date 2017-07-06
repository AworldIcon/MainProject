package com.coder.kzxt.video.beans;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */

public class CatalogueBean implements Serializable {


    /**
     * code : 200
     * message : ok
     * items : [{"id":171,"title":"2","sequence":1,"course_id":16562,"video":[{"id":76,"user_id":916412,"course_id":16562,"course_chapter_id":171,"sequence":5,"status":2,"title":"11111","desc":null,"type":4,"free":0,"vid":"","is_drag":0,"time_length":0,"gold_num":0,"is_see":0,"is_share":1,"source":1,"resource_url":"","create_time":1488360211,"update_time":1491361048,"delete_time":null,"is_preview":0}]},{"id":28,"title":"第二节","sequence":6,"course_id":16562,"video":[{"id":230,"user_id":916412,"course_id":16562,"course_chapter_id":166,"sequence":47,"status":2,"title":"111111111111111","desc":null,"type":1,"free":0,"vid":"","is_drag":0,"time_length":0,"gold_num":0,"is_see":0,"is_share":1,"source":0,"resource_url":"","create_time":1488767157,"update_time":1491361048,"delete_time":null,"is_preview":0}]},{"id":216,"title":"士大夫撒放","sequence":53,"course_id":16562,"video":[{"id":287,"user_id":916412,"course_id":16562,"course_chapter_id":220,"sequence":60,"status":2,"title":"卡萨丁发","desc":"[{\"images_b\":[\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_0_b.jpg\",\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_1_b.jpg\",\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_2_b.jpg\",\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_3_b.jpg\",\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_4_b.jpg\",\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_5_b.jpg\"],\"md5checksum\":\"a998331e6deb939bad42ec53ed8d916c\",\"tag\":\"wyzc\",\"mp4\":\"http:\\/\\/mpv.videocc.net\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_1.mp4\",\"tsfilesize2\":\"0\",\"tsfilesize1\":\"11677296\",\"title\":\"111111111.mp4\",\"df\":\"2\",\"times\":\"0\",\"mp4_1\":\"http:\\/\\/mpv.videocc.net\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_1.mp4\",\"vid\":\"daee6f6ed7afbebc207516cb04572a13_d\",\"mp4_2\":\"http:\\/\\/mpv.videocc.net\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_2.mp4\",\"cataid\":\"1433325097070\",\"swf_link\":\"http:\\/\\/player.polyv.net\\/videos\\/daee6f6ed7afbebc207516cb04572a13_d.swf\",\"source_filesize\":\"15850746\",\"status\":\"50\",\"seed\":\"1\",\"flv2\":\"http:\\/\\/plvod01.videocc.net\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_2.flv\",\"flv1\":\"http:\\/\\/plvod01.videocc.net\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_1.plv\",\"sourcefile\":\"\",\"playerwidth\":\"600\",\"hls\":[\"http:\\/\\/hls.videocc.net\\/daee6f6ed7\\/d\\/daee6f6ed7afbebc207516cb04572a13_1.m3u8\",\"http:\\/\\/hls.videocc.net\\/daee6f6ed7\\/d\\/daee6f6ed7afbebc207516cb04572a13_2.m3u8\"],\"default_video\":\"http:\\/\\/plvod01.videocc.net\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_2.flv\",\"duration\":\"00:04:14\",\"filesize\":[\"10209456\",\"0\"],\"first_image\":\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_0.jpg\",\"original_definition\":\"848x480\",\"context\":\"\",\"images\":[\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_0.jpg\",\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_1.jpg\",\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_2.jpg\",\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_3.jpg\",\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_4.jpg\",\"http:\\/\\/img.videocc.net\\/uimage\\/d\\/daee6f6ed7\\/3\\/daee6f6ed7afbebc207516cb04572a13_5.jpg\"],\"playerheight\":\"490\",\"ptime\":\"2017-04-01 16:40:14\"}]","type":1,"free":1,"vid":"daee6f6ed7afbebc207516cb04572a13_d","is_drag":0,"time_length":254,"gold_num":0,"is_see":0,"is_share":0,"source":0,"resource_url":"","create_time":1491036333,"update_time":1491361048,"delete_time":null,"is_preview":0}]}]
     */

    private int code;
    private String message;
    private List<ItemsBean> items;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean implements Serializable {
        /**
         * id : 171
         * title : 2
         * sequence : 1
         * course_id : 16562
         * video : [{"id":76,"user_id":916412,"course_id":16562,"course_chapter_id":171,"sequence":5,"status":2,"title":"11111","desc":null,"type":4,"free":0,"vid":"","is_drag":0,"time_length":0,"gold_num":0,"is_see":0,"is_share":1,"source":1,"resource_url":"","create_time":1488360211,"update_time":1491361048,"delete_time":null,"is_preview":0}]
         */

        private String id;
        private String title;
        private String sequence;
        private int course_id;
        private List<VideoBean> video;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        public int getCourse_id() {
            return course_id;
        }

        public void setCourse_id(int course_id) {
            this.course_id = course_id;
        }

        public List<VideoBean> getVideo() {
            return video;
        }

        public void setVideo(List<VideoBean> video) {
            this.video = video;
        }

        public static class VideoBean implements  Serializable {
            /**
             * id : 76
             * user_id : 916412
             * course_id : 16562
             * course_chapter_id : 171
             * sequence : 5
             * status : 2
             * title : 11111
             * desc : null
             * type : 4
             * free : 0
             * vid :
             * is_drag : 0
             * time_length : 0
             * gold_num : 0
             * is_see : 0
             * is_share : 1
             * source : 1
             * resource_url :
             * create_time : 1488360211
             * update_time : 1491361048
             * delete_time : null
             * is_preview : 0
             */

            private String id;
            private int user_id;
            private int course_id;
            private String course_chapter_id;
            private String sequence;
            private int status;
            private String title;
            private String type;
            private String free;
            private String vid;
            private String desc;
            private String file_url;
            private int is_drag;
            private String time_length;
            private String gold_num;
            private String is_see;
            private int is_share;
            private int source;
            private String resource_url;
            private String mediaUri;
            private int is_preview;
            private String last_location;
            private int isShowLock;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getCourse_id() {
                return course_id;
            }

            public void setCourse_id(int course_id) {
                this.course_id = course_id;
            }

            public String getCourse_chapter_id() {
                return course_chapter_id;
            }

            public void setCourse_chapter_id(String course_chapter_id) {
                this.course_chapter_id = course_chapter_id;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getSequence() {
                return sequence;
            }

            public void setSequence(String sequence) {
                this.sequence = sequence;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                if(TextUtils.isEmpty(type)){
                    return "";
                }else {
                    return type;
                }

            }

            public String getFile_url() {
                return file_url;
            }

            public void setFile_url(String file_url) {
                this.file_url = file_url;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getFree() {
                return free;
            }

            public void setFree(String free) {
                this.free = free;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public int getIs_drag() {
                return is_drag;
            }

            public void setIs_drag(int is_drag) {
                this.is_drag = is_drag;
            }

            public String getTime_length() {
                return time_length;
            }

            public void setTime_length(String time_length) {
                this.time_length = time_length;
            }

            public String getGold_num() {
                return gold_num;
            }

            public void setGold_num(String gold_num) {
                this.gold_num = gold_num;
            }

            public String getIs_see() {
                return is_see;
            }

            public void setIs_see(String is_see) {
                this.is_see = is_see;
            }

            public int getIs_share() {
                return is_share;
            }

            public void setIs_share(int is_share) {
                this.is_share = is_share;
            }

            public int getSource() {
                return source;
            }

            public void setSource(int source) {
                this.source = source;
            }

            public String getResource_url() {
                return resource_url;
            }

            public void setResource_url(String resource_url) {
                this.resource_url = resource_url;
            }

            public int getIsShowLock() {
                return isShowLock;
            }
            public void setIsShowLock(int isShowLock) {
                this.isShowLock = isShowLock;
            }

            public int getIs_preview() {
                return is_preview;
            }

            public void setIs_preview(int is_preview) {
                this.is_preview = is_preview;
            }

            public String getMediaUri() {
                return mediaUri;
            }
            public void setMediaUri(String mediaUri) {
                this.mediaUri = mediaUri;
            }

            public String getLast_location() {
                if(TextUtils.isEmpty(last_location)){
                    return "0";
                }
                return last_location;
            }

            public void setLast_location(String last_location) {
                this.last_location = last_location;
            }
        }
    }
}
