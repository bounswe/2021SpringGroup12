<template>
  <div id="nav">
    <router-link to="/">Home</router-link> |
    <router-link to="/anime">Anime</router-link> |
    <router-link to="/books">Books</router-link> |
    <router-link to="/homenameinfo">Name/Age information</router-link>|
    <router-link to="/movies_home/">Movies</router-link> |
    <router-link to="/issues">Issues</router-link> |
    <router-link to="/convert">Currency Conversion</router-link> |
    <router-link to="/quotes">Quotes</router-link> |
    <router-link to="/cocktails">Cocktails</router-link>
  </div>
  <router-view />
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
