<template>
  
	<p class="text-6xl font-bold text-center text-gray-500 animate-pulse">
        Type your name and country to get a guess of your age, and how many entries we have for your name!
     </p>
  
    <p>Name</p>
      <input
        type="text"
        placeholder="required"
        v-model="name"
     />
	<p>Country</p>
	  <input
		type="text"
		placeholder="optional"
		v-model="country"
	/>
	<p/>
	<button v-on:click="fetchData">Guess Age</button>
	  

    <!-- Start of error alert -->
    <div class="mt-12 bg-red-50" v-if="error">
      <h3 class="px-4 py-1 text-4xl font-bold text-white bg-red-800">
        {{ error.title }}
      </h3>
      <p class="p-4 text-lg font-bold text-red-900">{{ error.message }}</p>
    </div>
    <!-- End of error alert -->
	
	<!-- Start of result info -->
    <div class="mt-40" v-if="result">
      <p class="text-6xl font-bold text-center text-gray-500 animate-pulse">
        Name: {{ result.name }} <p/>
        Age: {{ result.age }} <p/>
		Country: {{ result.country }} <p/>
		Count: {{ result.count }}
      </p>
    </div>
    <!-- End of result info -->
  
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      name: "",
      country: "",
	  result: null,
	  error: null
    };
  },
  methods: {
    async fetchData() {

      try {
        this.error = null;
		
		var countryParam = "";
		var countryEmpty = this.country === null || this.country.match(/^ *$/) !== null
		if(!countryEmpty){
			countryParam = "&country="+this.country;
		}
		const headers = {
			"Content-Type": "application/json",
			"Access-Control-Allow-Origin": "*"
		  };

	
        const response = axios
          .get(`http://${process.env.VUE_APP_API_URL}/name_information?name=` + this.name + countryParam, { headers })
          .then((value) => {
			
            if (value.status === 200) {
              this.result = {
				name: value.data.name,
				age: value.data.age,
				count: value.data.count
			  };
			  if(!countryEmpty){
				this.result.country = value.data.country
			  }
            } else {
              
            }
          }, (error) => {
			this.result = null;
            this.error = 
              {
                title: error.statusText,
                message: error.response.data,
              };
		  });
      } catch (err) {
        if (err.response) {
          // client received an error response (5xx, 4xx)
		  this.result = null;
          this.error = {
            title: "Server Response",
            message: err.message,
          };
        } else if (err.request) {
          // client never received a response, or request never left
		  this.result = null;
          this.error = {
            title: "Unable to Reach Server",
            message: err.message,
          };
        } else {
          // There's probably an error in your code
		  this.result = null;
          this.error = {
            title: "Application Error",
            message: err.message,
          };
        }
      }
    },
  },
};
</script>
