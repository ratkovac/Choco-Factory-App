<template>
    <section class="py-5">
      <div class="container px-4 px-lg-5 my-5">
        <div class="row gx-5 gx-lg-1 align-items-center">
          <div class="col-md-12">
            <h1 class="display-7 fw-bolder">{{ title }}</h1>
            <div class="table-responsive">
              <table class="table table-striped table-bordered">
                <thead>
                  <tr>
                    <th class="border">Logo firme</th>
                    <th class="border">Naziv</th>
                    <th class="border">Lokacija</th>
                    <th class="border">Prosečna ocena</th>
                    <th class="border"></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="factory in factories" :key="factory.id">
                    <td class="border">
                      <img :src="factory.pathToLogo" alt="Factory Logo" style="max-width: 60px; max-height: 60px;">
                    </td>
                    <td class="border">{{ factory.name }}</td>
                    <td class="border">{{ factory.location }}</td>
                    <td class="border">{{ factory.rate }}</td>
                    <td class="border" style="padding: 0;">
                        <button @click="pregledajFabriku(factory)" class="btn btn-primary btn-view-factory">
                        Pregled fabrike
                        </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </section>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import { useRoute, useRouter } from 'vue-router';
  
  const route = useRoute()
  const router = useRouter()

  const factories = ref([]);
  const title = ref("All Chocolate Factories");
  
  onMounted(async () => {
    try {
      const response = await axios.get('http://localhost:8080/WebShopAppREST/rest/factories');
      factories.value = response.data;
    } catch (error) {
      console.error(error);
      title.value = "Error";
    }
  });
  
  const pregledajFabriku = (factory) => {
    router.push({ path: `/factories/${factory.id}` });
  };
  </script>
  
  <style scoped>
  .border {
    border: 1px solid #dee2e6;
  }
  
  .btn-view-factory {
    background-color: cadetblue;
    height: 50px;
    width: 100%;
    font-size: 15px;
    font-size: 16px;
    color: white;
  }
  </style>
  