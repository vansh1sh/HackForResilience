package snap.hackforresilience;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Count {
        @SerializedName("count")
        @Expose
        private String count;
        @SerializedName("location")
        @Expose
        private String location;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
}