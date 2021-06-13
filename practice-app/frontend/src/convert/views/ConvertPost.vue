<template>
  <link
    rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
  />
  <p class="error" v-if="this.error !==''">{{ this.error }}</p>
  <form id="convertForm" @submit.prevent="convert">
    Date:<input type="text" v-model="date" />
    From:<input type="text" v-model="from" />
    To:<input type="text" v-model="to" />
    Rate:<input type="text" v-model="rate" />
    <button type="submit">Add</button>
  </form>
  <div class="anime">
  <p class="result" v-if="this.result!==''">Result: {{ this.result}}</p>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      from: '',
      to: '',
      rate: '',
      date: '',
      error: '',
      result: '',
    };
  },
  methods: {
    async convert() {
    try{
      const url = `http://${process.env.VUE_APP_API_URL}/convert/`;
      let response = await axios.post(url, {
        date: this.date,
        from: this.from,
        to: this.to,
        rate: this.rate
      });
      if(response.status != 200){
        console.log("Error: ", JSON.stringify(response.data));
      }else{
        console.log("Success: ", response.data.result);
        this.result = response.data;
      }
      }catch(err){
        this.error = err.response.data;
      }
    },
  },
};
</script>
