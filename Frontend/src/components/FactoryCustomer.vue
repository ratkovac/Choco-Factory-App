<template>
  <div>
    <!-- Progress Bar -->
    <div class="progress-container">
      <div class="progress-step active">Odabir fabrike</div>
      <div class="progress-step" :class="{ active: isFactorySelected }">Odabir čokolade</div>
      <div class="progress-step" :class="{ active: cartShow }">Pregled i potvrda</div>
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
    
    <div v-if="chocolatesShow">
      <!-- Selected Factory Details and Chocolates -->
      <section class="py-5" style="width: 1200px;">
        <div class="row">
          <div class="col-md-30 d-flex align-items-center" style="margin-left: 0px; margin-bottom: 30px;">
            <img :src="selectedFactory.pathToLogo" alt="Factory Logo" style="max-width: 100px;">
            <div>
              <h2 style="font-weight: bold;"> {{ selectedFactory.name }}</h2>
            </div>
            <button @click="pregledajKorpu()" class="btn btn-primary btn-add-to-cart" style="margin-left: 800px; background-color: white; font-weight: bold; border-color: rgb(64, 151, 249); color: rgb(64, 151, 249);">
              Pregled Korpe
            </button>
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
    
    <div v-if="cartShow" class="cart-section">
    <!-- Pregled Korpe -->
    <h2>Pregled Korpe</h2>
    <div v-if="cart.chocolates.length > 0" class="cart-table-container">
      <button @click="buy()" style="margin-left: 950px; width: 160px;" class="btn btn-primary btn-add-to-cart">Poruci</button>
      <p style="font-weight: bold; margin-left: 950px">Ukupna cena: {{ totalPrice }} RSD</p>
      <table class="custom-table">
        <thead>
          <tr>
            <th>Čokolada</th>
            <th>Količina</th>
            <th>Cena</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="chocolate in cart.chocolates" :key="chocolate.id">
            <td>{{ getChocolateName(chocolate.id) }}</td>
            <td>{{ chocolate.quantity }}</td>
            <td>{{ getChocolateCost(chocolate.id) * chocolate.quantity }} RSD</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else>
      <p>Korpa je prazna.</p>
    </div>
  </div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

const title = ref("Fabrike čokolade");

const cartShow = ref(false);
const chocolatesShow = ref(false);
const factories = ref([]);
const filteredFactories = ref([]);
const selectedFactory = ref(null);
const chocolates = ref([]);
const chocolatesList = ref([]);
const comments = ref([]);
const isFactorySelected = ref(false);

onMounted(async () => {
  await loadFactories();
  this.userId = route.query.userId;
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

const pregledajKorpu = () => {
  cartShow.value = true;
  chocolatesShow.value = false;
};

const selectFactory = async (factory) => {
  selectedFactory.value = factory;
  isFactorySelected.value = true;
  chocolatesShow.value = true;
  await loadFactoryDetails(factory.id);
};

const getChocolateName = (id) => {
  const chocolate = chocolates.value.find(choco => choco.id === id);
  return chocolate ? chocolate.name : 'Nepoznata čokolada';
};

const getChocolateCost = (id) => {
  const chocolate = chocolates.value.find(choco => choco.id === id);
  return chocolate ? chocolate.cost : 0;
};

const loadFactoryDetails = async (factoryId) => {
  try {
    const chocolatesResponse = await axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/factory/${factoryId}`);
    chocolates.value = chocolatesResponse.data.map(chocolate => ({ ...chocolate, quantity: 1 }));
    chocolatesList.value = chocolatesResponse.data;
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

const buy = async () => {
  try {
    const response = await axios.post(`http://localhost:8080/WebShopAppREST/rest/carts/`, cart.value);
  } catch(error) {
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

const userId = ref(""); // Postaviti odgovarajući userId
const cart = ref({
  id: "",
  userId: route.query.id,
  price: "",
  chocolates: []
});
const totalPrice = ref(0); // Implementacija prema potrebi

const addToCart = (chocolate) => {
  const priceToAdd = chocolate.cost * chocolate.quantity;
  totalPrice.value += priceToAdd;
  cart.value.price = totalPrice.value;

  // Provera da li čokolada već postoji u korpi
  const existingChocolate = cart.value.chocolates.find(item => item.id === chocolate.id);
  if (existingChocolate) {
    existingChocolate.quantity += chocolate.quantity;
  } else {
    cart.value.chocolates.push({ id: chocolate.id, quantity: chocolate.quantity });
  }
  console.log("Cart userID:", cart.value.userId);
  console.log("Cart price:", cart.value.price);
  console.log("Cart:", cart.value.userId);
  console.log("Cart choco:", cart.value.chocolates);
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

.cart-section {
  width: 1200px; /* Isto kao i kod drugih sekcija */
  margin-top: 20px; /* Dodaj margine po potrebi */
}

.cart-table-container {
  background-color: #f8f9fa; /* Boja pozadine, možeš promijeniti po želji */
  padding: 20px; /* Dodaj padding za bolji izgled */
  border-radius: 10px; /* Dodaj obrubljenje za bolji izgled */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Dodaj sjenu za bolji izgled */
}

</style>
