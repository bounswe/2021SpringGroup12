
<template>
  <div class="login">
    <b>New Entry</b><br />
	
    <b>Please enter your information to save our name/age database! If succeded, it will be included to the age guess part</b><br />
	
    <form class="postForm" @submit.prevent="post">
      <p>Name</p>
      <input
        type="text"
        placeholder="required"
        v-model="request.name"
      />
      <p>Age</p>
      <input
        type="number"
        placeholder="required"
        v-model="request.age"
      />
      <p>Country</p>
      <input type="text"
		placeholder="required"	  
		v-model="request.country" />
      <br /><br />
      <br />
      <button type="submit">Post</button>
    </form>

	<h1 v-if="searchError">Please dont leave any fields empty!</h1>
    <h1 v-if="success">{{ this.message }}</h1>
    <h1 v-if="fail">{{ this.message }}</h1>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      success: false,
      fail: false,
      message: null,
      searchError: null,
      request: {
        name: null,
        age: null,
        country: null
      },
    };
  },
  methods: {
    async post() {
	  this.searchError = false;
      this.success = false;
      this.message = "";
      this.fail = false;
      const url = `http://127.0.0.1:5000/name_information`;
	  
	  
      if (!this.request.name || 
			!this.request.age || 
			!this.request.country || 
			!this.request.name.length || 
			!this.request.country.length ||
			this.request.age<=0) {
		  this.searchError = true;
		  return;
	  }

      const response = await axios
        .post(url, this.request)
        .then((value) => 
		{
          if (value.status == 200) {
			this.message = value.data;
            this.success = true;
            this.fail = false;
          }
        })
        .catch((value) => {
          this.success = false;
          this.fail = true;
          this.messsage = value.response.data;
        });
    },
  },
};
</script>

<style>
.postForm {
  display: block;
}
</style>
