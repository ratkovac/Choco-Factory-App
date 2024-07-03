<template>
  <div>
    <!-- Progress Bar -->
    <div class="progress-container">
      <div class="progress-step active">Odabir fabrike</div>
      <div class="progress-step" :class="{ active: isFactorySelected }">Odabir čokolade</div>
      <div class="progress-step">Pregled i potvrda</div>
    </div>
    
    <!-- Conditional Rendering -->
    <div v-if="!isFactorySelected">
      <!-- Factories List -->
      <div class="factories-list row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4" style="margin-top: 10px;">
        <div class="col" v-for="factory in sortedFactories" :key="factory.id">
          <div class="card h-100" @click="selectFactory(factory)">
            <img :src="factory.pathToLogo" class="card-img-top img-thumbnail" alt="Factory Logo">
            <div class="card-body">
              <h5 class="card-title">{{ factory.name }}</h5>
              <p class="card-text location">Lokacija: {{ factory.location }}</p>
              <p class="card-text">Prosečna ocena: {{ factory.rate }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <div v-else>
      <!-- Selected Factory Details and Chocolates -->
      <section class="py-5" style="width: 1200px;">
        <div class="row">
          <div class="col-md-30 d-flex align-items-center" style="margin-left: 0px; margin-bottom: 30px;">
            <img :src="selectedFactory.pathToLogo" alt="Factory Logo" style="max-width: 100px;">
            <div>
              <h2 style="font-weight: bold;"> {{ selectedFactory.name }}</h2>
            </div>
          </div>

          <div class="col-md-30">
              <span v-if="!(chocolates && chocolates.length > 0)"> Nema dostupnih čokolada</span>
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4" v-if="chocolates && chocolates.length > 0">
              <div class="col" v-for="chocolate in chocolates" :key="chocolate.id">
                <div class="card h-100">
                  <img :src="chocolate.picture" class="card-img-top img-thumbnail" alt="Slika čokolade">
                  <div class="card-body">
                    <h5 class="card-title">{{ chocolate.name }}</h5>
                    <p class="card-text">Masa: {{ chocolate.mass }}g</p>
                    <p class="card-text">Cena: {{ chocolate.cost }} RSD</p>
                    <div class="quantity-input">
                      <label for="quantity">Količina:</label>
                      <input type="number" v-model.number="chocolate.quantity" :max="chocolate.stock" min="1" @input="validateQuantity(chocolate)">
                    </div>
                    <button @click="addToCart(chocolate)" class="btn btn-primary btn-add-to-cart">Dodaj u korpu</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

const title = ref("Fabrike čokolade");

const factories = ref([]);
const filteredFactories = ref([]);
const selectedFactory = ref(null);
const chocolates = ref([]);
const comments = ref([]);
const isFactorySelected = ref(false);

onMounted(async () => {
  await loadFactories();
});

const loadFactories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/WebShopAppREST/rest/factories');
    factories.value = response.data;
    filterFactories();
  } catch (error) {
    console.error(error);
    title.value = "Greška u učitavanju";
  }
};

const filterFactories = () => {
  filteredFactories.value = factories.value.filter(factory => {
    // Implement your filtering logic here
    return true; // Placeholder
  });
};

const selectFactory = async (factory) => {
  selectedFactory.value = factory;
  isFactorySelected.value = true;
  await loadFactoryDetails(factory.id);
};

const loadFactoryDetails = async (factoryId) => {
  try {
    const chocolatesResponse = await axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/factory/${factoryId}`);
    chocolates.value = chocolatesResponse.data.map(chocolate => ({ ...chocolate, quantity: 1 }));
    const commentsResponse = await axios.get(`http://localhost:8080/WebShopAppREST/rest/comments/factory/${factoryId}`);
    comments.value = commentsResponse.data;
  } catch (error) {
    console.error(error);
  }
};

const addChocolate = (factory) => {
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

const sortedFactories = computed(() => {
  return filteredFactories.value;
});

const validateQuantity = (chocolate) => {
  if (chocolate.quantity > chocolate.stock) {
    chocolate.quantity = chocolate.stock;
  }
  if (chocolate.quantity < 1) {
    chocolate.quantity = 1;
  }
};

const addToCart = async (chocolate) => {
  try {
    const response = await axios.delete(`http://localhost:8080/WebShopAppREST/rest/carts/`);
    chocolates.value = await getChocolatesByFactory(factory.id);
  } catch (error) {
    console.error(error);
  }
};
</script>

<style scoped>
.progress-container {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.progress-step {
  flex: 1;
  padding: 10px;
  text-align: center;
  border-bottom: 3px solid lightgray;
}

.progress-step.active {
  font-weight: bold;
  border-bottom: 4px solid green;
  font-size: 20px;
}

.card {
  background-color: rgb(64, 151, 249);
  width: 100%;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.card:hover {
  transform: scale(1.05);
}

.card-img-left {
  max-width: 150px;
  object-fit: cover;
  float: left;
  margin-right: 10px;
}

.card-title {
  color: white;
  font-weight: bold;
  margin-bottom: 1rem; /* Povećan razmak ispod naslova */
}

.card-text {
  color: white;
  font-weight: bold;
}

.card-text.location {
  margin-bottom: 0.5rem; /* Smanjen razmak između lokacije i prosečne ocene */
}

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

.quantity-input {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.quantity-input label {
  margin-right: 10px;
  color: white;
}

.quantity-input input {
  width: 60px;
}
</style>
