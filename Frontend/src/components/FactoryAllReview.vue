<template>
  <section class="py-5">
    <div class="container px-4 px-lg-5 my-5">
      <div class="row gx-5 gx-lg-1 align-items-center">
        <div class="col-md-12">
          <div class="card mb-3">
            <div class="card-body" style="background-color: aliceblue;">
              <div class="row">
                
                <div >
                  <div>
                  <img :src="factory.pathToLogo" alt="Factory Logo" class="factory-logo img-fluid">
                </div>
                  <h2><strong>Naziv:</strong> {{ factory.name }}</h2>
                  <p><strong>Radno vreme:</strong> {{ factory.workingTime }}</p>
                  <p><strong>Lokacija:</strong> {{ location.address }}</p>
                  <p v-if="factory.rate"><strong>Ocena:</strong> {{ factory.rate }}</p>
                  <p v-if="factory.status"><strong>Status:</strong> {{ factory.status }}</p>
                  <p><strong>Komentari:</strong>
                    <div v-if="comments && comments.length > 0">
                      <div v-for="comment in comments" :key="comment.id">{{ comment.text }}</div>
                    </div>
                    <span v-else>Nema komentara</span>
                  </p>
                  <p><strong>Čokolade:</strong>
                    <span v-if="!(chocolates && chocolates.length > 0)">Nema dostupnih čokolada</span>
                  </p>
                  <div class="table-responsive" v-if="chocolates && chocolates.length > 0">
                    <table class="table table-bordered custom-table">
                      <thead class="thead-dark">
                        <tr>
                          <th>Slika</th>
                          <th>Naziv</th>
                          <th>Cena</th>
                          <th>Kategorija</th>
                          <th>Tip</th>
                          <th>Masa</th>
                          <th>Detalji</th>
                          <th>Zaliha</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="chocolate in chocolates" :key="chocolate.id">
                          <td>
                            <img :src="chocolate.picture" alt="Slika čokolade" class="chocolate-image img-thumbnail">
                          </td>
                          <td>{{ chocolate.name }}</td>
                          <td>{{ chocolate.cost }}</td>
                          <td>{{ chocolate.category }}</td>
                          <td>{{ chocolate.type }}</td>
                          <td>{{ chocolate.mass }}</td>
                          <td>{{ chocolate.details }}</td>
                          <td>{{ chocolate.stock }}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <span v-else>Nema dostupnih čokolada</span>
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

const route = useRoute();
const router = useRouter();

const factory = ref({
  name: '',
  locationId: '',
  pathToLogo: '',
  rate: 0,
  isDeleted: false,
  workingTime: ''
});
const chocolates = ref([]);
const comments = ref([]);
const location = ref({
  id: '',
  longitude: '',
  latitude: '',
  address: ''
});

const getCommentsByFactory = async (factoryId) => {
  try {
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/comments/factory/valid/${factoryId}`);
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

const getLocationById = async (locationId) => {
  try {
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/location/${locationId}`);
    return response.data;
  } catch (error) {
    console.error(error);
    return {};
  }
};

onMounted(async () => {
  try {
    const factoryId = route.params.id;
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/${factoryId}`);
    factory.value = response.data;
    chocolates.value = await getChocolatesByFactory(factoryId);
    comments.value = await getCommentsByFactory(factoryId);
    location.value = await getLocationById(factory.value.locationId);
  } catch (error) {
    console.error(error);
  }
});

const addChocolate = (factory) => {
  router.push({ path: `/chocolates/factories/${factory.id}/${factory.name}` });
};

const updateChocolate = async (chocolateId, factory) => {
  try {
    await axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/${chocolateId}`);
    router.push({ path: `/chocolates/${chocolateId}/${factory.name}` });
  } catch (error) {
    console.error(error);
  }
};

const deleteChocolate = async (chocolateId, factory) => {
  try {
    await axios.delete(`http://localhost:8080/WebShopAppREST/rest/chocolates/${chocolateId}`);
    chocolates.value = await getChocolatesByFactory(factory.id);
  } catch (error) {
    console.error(error);
  }
};
</script>

<style scoped>
.factory-logo {
  max-width: 100px;
  margin-bottom: 20px;
}

.custom-table th,
.custom-table td {
  vertical-align: middle;
}

.chocolate-image {
  width: 50px;
  height: 50px;
  display: block;
}

.thead-dark th {
  background-color: #343a40;
  color: white;
}

.table {
  background-color: #f8f9fa;
}

.card {
  border: 1px solid #343a40;
}

.card-body {
  background-color: #f8f9fa;
}

h2, p {
  color: #343a40;
}
</style>