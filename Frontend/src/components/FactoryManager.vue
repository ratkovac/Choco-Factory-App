<template>
    <section class="py-5">
      <div class="container px-4 px-lg-5 my-5">
        <div class="row gx-5 gx-lg-1 align-items-center">
          <div class="col-md-12">
            <div class="card mb-3">
              <div class="card-body">
                <div class="row">
                  <div class="col-md-3">
                    <img :src="factory.pathToLogo" alt="Factory Logo" style="max-width: 100px;">
                  </div>
                  <div class="col-md-9">
                    <h2><strong>Naziv:</strong> {{ factory.name }}</h2>
                    <p><strong>Radno vreme:</strong> {{ factory.workingTime }}</p>
                    <p><strong>Lokacija:</strong> {{ factory.location }}</p>
                    <p v-if="factory.rate"><strong>Ocena:</strong> {{ factory.rate }}</p>
                    <p v-if="factory.status"><strong>Status:</strong> {{ factory.status }}</p>
                    <p><strong>Komentari:</strong>
                      <ul v-if="comments && comments.length > 0">
                        <li v-for="comment in comments" :key="comment.id">{{ comment.text }}</li>
                      </ul>
                      <span v-else>Nema komentara</span>
                    </p>
                    <p><strong>Cokolade:</strong>
                        <span v-if="!(chocolates && chocolates.length > 0)"> Nema dostupnih čokolada</span>
                    </p>
                    <div class="table-responsive" v-if="chocolates && chocolates.length > 0">
                        <table v-if="chocolates && chocolates.length > 0" class="custom-table">
                            <thead>
                                <tr>
                                    <th class="border" style="width: 80px;">Slika</th>
                                    <th class="border">Naziv</th>
                                    <th class="border">Cena</th>
                                    <th class="border">Kategorija</th>
                                    <th class="border">Tip</th>
                                    <th class="border">Masa</th>
                                    <th class="border">Detalji</th>
                                    <th class="border">Zaliha</th>
                                    <th class="border"></th> <!-- Dodajte praznu ćeliju za dugmad -->
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="chocolate in chocolates" :key="chocolate.id">
                                    <td class="border" style="padding: 0; width: 50px; height: 50px;">
                                        <img :src="chocolate.picture" alt="Slika čokolade" style="width: 100%; height: 100%; display: block;" />
                                    </td>
                                    <td class="border">{{ chocolate.name }}</td>
                                    <td class="border">{{ chocolate.cost }}</td>
                                    <td class="border">{{ chocolate.category }}</td>
                                    <td class="border">{{ chocolate.type }}</td>
                                    <td class="border">{{ chocolate.mass }}</td>
                                    <td class="border" style="word-wrap: break-word;">{{ chocolate.details }}</td>
                                    <td class="border">{{ chocolate.stock }}</td>
                                    <td class="border">
                                      <button @click="updateChocolate(chocolate.id, factory)" class="btn btn-primary btn-update">Ažuriraj</button>
                                        <button @click="deleteChocolate(chocolate.id, factory)" class="btn btn-danger btn-update">Obriši</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <span v-else>Nema dostupnih čokolada</span>
                    </div>

                    <button @click="addChcolate(factory)" class="btn btn-primary btn-view-factory">
                        Dodaj cokoladu
                        </button>
                  </div>
                </div>
              </div>
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

  const factory = ref({
  name: '',
  location: '',
  pathToLogo: '',
  rate: 0,
  isDeleted: false,
  workingTime: ''
  });
  const chocolates = ref([]);
  const comments = ref([]);


  const getCommentsByFactory = async (factoryId) => {
    console.log("Uslo1");
  try {
    console.log(factoryId);
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/comments/factory/${factoryId}`);
    return response.data; 
  } catch (error) {
    console.error(error);
    return [];
  }
};

  const getChocolatesByFactory = async (factoryId) => {
    try {
      const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/factory/${factoryId}`);
      return response.data; 
    } catch (error) {
      console.error(error);
      return [];
    }
  };

  
onMounted(async () => {
  try {
    const factoryId = route.params.id; 
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/${factoryId}`);
    factory.value = response.data;
    chocolates.value = await getChocolatesByFactory(factoryId);
    comments.value = await getCommentsByFactory(factoryId);
  } catch (error) {
    console.error(error);
  }
});

const addChcolate = (factory) => {
    router.push({ path: `/chocolates/factories/${factory.id}/${factory.name}` });
  };

  const updateChocolate = async (chocolateId, factory) => {
    try {
        const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/${chocolateId}`);
        const chocolateData = response.data;

        router.push({ path: `/chocolates/${chocolateId}/${factory.name}` });
    } catch (error) {
        console.error(error);
    }
  };

  const deleteChocolate = async (chocolateId, factory) => {
    try {
      const response = await axios.delete(`http://localhost:8080/WebShopAppREST/rest/chocolates/${chocolateId}`); 
      chocolates.value = await getChocolatesByFactory(factory.id);
    } catch (error) {
      console.error(error);
    }
  };
  </script>
  
  <style scoped>
.border {
    border: 1px solid #dee2e6;
  }

  .custom-table {
    width: 100%;
    margin-bottom: 1rem;
    background-color: transparent;
    border-collapse: collapse;
  }

  .custom-table th,
  .custom-table td {
    padding: 0.75rem;
    vertical-align: top;
    border-top: 1px solid #dee2e6;
  }

  .custom-table thead th {
    vertical-align: bottom;
    border-bottom: 2px solid #dee2e6;
  }

  .custom-table tbody + tbody {
    border-top: 2px solid #dee2e6;
  }

  .btn-view-factory {
    background-color: cadetblue;
    height: 50px;
    width: auto;
    font-size: 15px;
    font-size: 16px;
    color: white;
  }
  .btn-update {
    background-color: cadetblue;
    height: 25px;
    width: auto;
    font-size: 12px;
    color: white;
  }

  </style>
  