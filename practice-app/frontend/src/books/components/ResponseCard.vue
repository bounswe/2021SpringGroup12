<template>
  <section class="p-4 rounded-lg shadow-lg bg-gray-50 w-80">
    <div class="h-100">
      <p class="word-wrap" v-for="(value, name) in post" v-bind:key="value">
        
        
        <label class="text-l font-bold text-green-900">  <b> {{ name }}:  </b> </label>
        
        
        <label v-if="Array.isArray(value)">
          <label v-for="(value1, index) in value" v-bind:key="index">
            
            <label class="text-l decoration-underline text-green-500">
              <br />{{ index + 1 }}:
            </label>
            <label class="text-m font-italic text-green-900">
              <label v-if="!Array.isArray(value)">
                <label v-for="(value2, index2) in value1" v-bind:key="index2">
                  <label class="text-l decoration-underline text-green-500">
                    <br />{{ index2 + 1 }}:
                  </label>
                  <label class="text-m font-italic text-green-900">
                    {{ value2 }} <br />
                  </label>
                </label>
              </label>
              <label v-else>
                {{ value1 }}
              </label>
            </label>
            <!-- we can change color, I am not very good at it -->
          </label>
        </label>
        <label v-else> {{ value }}<br/> <br/> </label>
      </p>
        <br/>
  <br/>
  <br/>
  <br/>
  <br/>
  
    </div>
  </section>
</template>

<script>
export default {
  props: {
    post: {
      type: Object,
      required: true,
    },
  },
}
</script>

<style>
p.word-wrap {
  word-wrap: break-word;
}
</style>