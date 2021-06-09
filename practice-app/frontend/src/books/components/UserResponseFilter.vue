<template>
  <div class="flex justify-center p-4 rounded">
    <!-- Start of select dropdown -->
    <div class="relative inline-flex">
      <select
        class="
          h-10
          pl-5
          pr-10
          text-gray-600
          bg-white
          border border-gray-300
          rounded-lg
          appearance-none
          hover:border-gray-400
          focus:outline-none
        "
        v-model="section"
      >
        <option
          v-for="(section, index) in sections"
          :key="index"
          :value="section"
        >
          {{ capitalize(section) }}
        </option>
        <br /><br />
      </select>
    </div>
    <!-- End of select dropdown -->
    &nbsp; &nbsp; &nbsp;&nbsp;
    <div class="relative inline-flex rounded-pill">
      <textarea v-model="message" placeholder="add multiple lines"></textarea>

      <!-- <input v-model="message" :message="message" /> -->
    </div>
    <div class="self-center ml-8">
      <br />
      <button
        class="px-6 py-2 text-white bg-green-700 rounded hover:bg-green-900"
        @click="fetch"
      >
        Retrieve
      </button>
    </div>
  </div>
</template>

<script>
import { computed } from "vue";
import sectionsData from "./user_sections";

export default {
  props: {
    modelValue: Array,
    fetch: Function,
  },
  setup(props, { emit }) {
    const message = computed({
      get: () => props.modelValue,
      set: (value) => emit("update:modelValue", value),
    });
    const section = computed({
      get: () => props.modelValue,
      set: (value) => emit("update:modelValue", value),
    });

    return {
      section,
      message,
    };
  },
  data() {
    return {
      sections: sectionsData,
    };
  },
  methods: {
    capitalize(value) {
      if (!value) return "";
      value = value.toString();
      return value;
    },
  },
};
</script>
