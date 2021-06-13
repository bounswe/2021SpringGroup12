<template>
<body>
    <h3> ADD MOVIE REVIEW </h3>
    <form v-if="!sent" class="postForm" @submit.prevent="AddReview">
        <div class="form-group">
            <!--
                movie.display_title, movie.mpaa_rating, movie.critics_pick, movie.byline, movie.headline, movie.summary_short, movie.link
            -->
            <title>Add a Review</title>
            <div>
                <label for="display_title"> Movie Title : </label>
                <input v-model="user_body.display_title"
                    placeholder="Enter Movie Title" />
                <br>
            </div>
            <div>
                <label for="mpaa_rating"> Mpaa Rating : </label>
                <input v-model="user_body.mpaa_rating"
                    placeholder="Enter Mpaa Rating" />
                <br>
            </div>
            <div>
                <label for="critics_pick">Critics Pick :</label>
                <input v-model="user_body.critics_pick"
                    placeholder="Enter Critics Pick as 1 or 0" />
                <br>
            </div>
            <div>
                <label for="byline">Author of the review :</label>
                <input v-model="user_body.byline" placeholder="Enter the Author" />
                <br>
            </div>
            <div>
                <label for="headline"> Headline of the review : </label>
                <input v-model="user_body.headline"
                    placeholder="Enter Headline of the review" />
                <br>
            </div>
            <div>
                <label for="summary_short">Short summary about the review :</label>
                <input v-model="user_body.summary_short"
                    placeholder="Enter a short summary about the review" size=100 style="height:200px" />
                <br>
            </div>
            <div>
                <label for="link">Review URL :</label>
                <input v-model="user_body.link" placeholder="Enter the Review URL" />
                <br>
            </div>
            <button type="submit">Add review</button>
            <br>
            <br>
            <br>
            <br>
        </div>
    </form>
    <h1 v-if="success">{{ this.response }}</h1>
    <h1 v-if="fail">{{ this.error }}</h1>

</body>

</template>


 
<script> 

import axios from "axios";

export default {
  data() {
    return {
      sent: false,
      success: false,
      fail: false,
      response: "",
      error: null,
      searchQuery: "",
      searchError: null,
      user_body: {
        display_title: "",
        mpaa_rating: "",
        critics_pick: "0",
        byline: "",
        headline: "",
        summary_short: "",
        link: "",
      },
    };
  },
    setup() {
        
    },
    methods: {
      async AddReview() {
      this.sent = false;
      this.success = false;
      this.response = "";
      this.fail = false;
      this.error = null;
      const url = `http://${process.env.VUE_APP_API_URL}/movies_addReview/`;


      if (
        !this.user_body.display_title.replace(" ", "").length ||
        !this.user_body.link.replace(" ", "").length ||
        !this.user_body.byline.replace(" ", "").length
      ) {
        this.success = false;
        this.fail = true;
        this.error = "Please fill title,reviewer name and link fields!";
      }
      else {
        const response = await axios
          .post(url, this.user_body)
          .then((value) => {
            if (value.status == 200) {
              this.response = value.data;
              this.success = true;
              this.fail = false;
            }
          })
          .catch((value) => {
            this.success = false;
            this.fail = true;
            this.error = value.response.data;
          });
      }
      /*if (!this.user_body.assignees.length) {
        this.user_body.assignees = [];
      } else {
        this.user_body.assignees = this.user_body.assignees.split(",");
      }
      if (!this.user_body.labels.length) {
        this.user_body.labels = [];
      } else {
        this.user_body.labels = this.user_body.labels.split(",");
      }*/

      if (this.success) {
        this.sent = true;
      }
    },
  },
}
</script>
