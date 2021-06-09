

<template>
    <div id="nav">
        <router-link to="/movies_addReview/">Add new review </router-link> 
        <br>
        <br>
        <br>
        <router-link to="/movies/">Search for Keyword </router-link>
    </div>
  <router-view/>
</template>

<script>
export default {
    setup() {
        
    },
    submitClick(){
        this.$router.push({path: '/movies/', query:{keyword: this.keyword}})
    }
}
</script>