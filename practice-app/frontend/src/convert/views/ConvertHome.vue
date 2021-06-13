<template>
  <link
    rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
  />
  <h2>GET</h2>
  <p> You can calculate the money amount that you have in another currency </p>
  <p class="error" v-if="this.error !==''">{{ this.error }}</p>
  <form id="convertForm" @submit.prevent="convert">
    From:<input type="text" v-model="fromQuery" />
    To:<input type="text" v-model="toQuery" />
    Amount:<input type="text" v-model="amountQuery" />
    <button type="submit">Convert</button>
  </form>
  <h2>POST</h2>
  <p> You can post a currency exchange rate for a specific date. </p>
  <form id="convertForm" @submit.prevent="postF">
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
      fromQuery: '',
      toQuery: '',
      amountQuery: '',
      date: '',
      from: '',
      to: '',
      rate: '',
      error: '',
      result: '',
    };
  },
  methods: {
    async convert() {
    this.error = '';
    this.result = '';
    try{
      const url = `http://${process.env.VUE_APP_API_URL}/convert/?from=${this.fromQuery}&to=${this.toQuery}&amount=${this.amountQuery}`;
      let response = await axios.get(url);
      if(response.status != 200){
        console.log("Error: ", JSON.stringify(response.data));
      }else{
        console.log("Success: ", response.data.result);
        this.result = response.data.result;
      }
      }catch(err){
        this.error = err.response.data;
      }
    },
    async postF() {
    try{
    this.error = '';
    this.result = '';
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
