<template>
  <!-- Search Bars -->
  <h1 class="display-5 fw-bold mb-4" style="text-align: center">{{ title }}</h1>
  <div class="row gx-5 gx-lg-1 align-items-center mb-2">
    <div class="col-md-2 mb-2">
      <input v-model="searchParams.factoryName" type="text" class="form-control" placeholder="Naziv fabrike" @input="filterFactories">
    </div>
    <div class="col-md-2 mb-2">
      <input v-model="searchParams.chocolateName" type="text" class="form-control" placeholder="Ime čokolade" @input="filterFactories">
    </div>
    <div class="col-md-2 mb-2">
      <input v-model="searchParams.address" type="text" class="form-control" placeholder="Lokacija" @input="filterFactories">
    </div>
    <div class="col-md-2 mb-2">
      <input v-model="searchParams.averageRating" type="number" class="form-control" placeholder="Prosečna ocena" @input="filterFactories">
    </div>
    <div class="col-md-2 mb-2">
      <select v-model="sortParams.criterion" class="form-select" aria-label="Default select example">
        <option value="">Sortiraj po...</option>
        <option value="name">Naziv</option>
        <option value="address">Adresa</option>
        <option value="rate">Prosečna ocena</option>
      </select>
    </div>
    <div class="col-md-2 mb-2">
      <select v-model="sortParams.ascending" class="form-select" aria-label="Default select example">
        <option value="true">Rastuće</option>
        <option value="false">Opadajuće</option>
      </select>
    </div>
  </div>

  <!-- Filter -->
  <div class="row gx-1 gx-lg-1 align-items-center mb-3">
    <div class="d-inline-flex gap-2">
      <button class="btn btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample" style="font-size: x-large; width: 150px;">
        Filter
      </button>
      <button @click="searchFactories" class="btn btn-primary" style="font-size: x-large; width: 150px;">
        Pretraži
      </button>
    </div>
    <div class="collapse mt-3" id="collapseExample">
      <div class="row gx-5 gx-lg-1 align-items-center">
        <div class="col-md-3">
          <div>
            <h6 class="fw-bold">Chocolate category:</h6>
            <div class="form-check form-check-inline">
              <label class="form-check-label">
              <input class="form-check-input" type="checkbox" v-model="chocolateCategoryFilters.regular" @change="updateChocolateCategoryFilters">
                Regular
              </label>
            </div>
            <div class="form-check form-check-inline">
              <label class="form-check-label">
              <input class="form-check-input" type="checkbox" v-model="chocolateCategoryFilters.cooking" @change="updateChocolateCategoryFilters">
                Cooking
              </label>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div>
            <h6 class="fw-bold">Chocolate type:</h6>
            <div class="form-check form-check-inline">
              <label class="form-check-label">
              <input class="form-check-input" type="checkbox" v-model="chocolateTypeFilters.milk" @change="updateChocolateTypeFilters">
                Milk
              </label>
            </div>
            <div class="form-check form-check-inline">
              <label class="form-check-label">
              <input class="form-check-input" type="checkbox" v-model="chocolateTypeFilters.dark" @change="updateChocolateTypeFilters">
                Dark
              </label>
            </div>
            <div class="form-check form-check-inline">
              <label class="form-check-label">
              <input class="form-check-input" type="checkbox" v-model="chocolateTypeFilters.white" @change="updateChocolateTypeFilters">
                White
              </label>
            </div>
          </div>
        </div>
        <div class="col-md-4 mb-2 gx-5">
          <label class="fw-bold">
            <input type="checkbox" v-model="searchParams.isOpen" @change="filterFactories"> Prikaži samo otvorene
          </label>
        </div>
      </div>
    </div>
  </div>

  <!-- Factories Table -->
  <div class="table-container" style="height: 500px; overflow-y: auto;">
    <table class="table table-hover">
      <thead class="table-dark">
        <tr>
          <th scope="col">Logo firme</th>
          <th scope="col">Naziv</th>
          <th scope="col">Lokacija</th>
          <th scope="col">Prosečna ocena</th>
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="factory in sortedFactories" :key="factory.id">
          <td>
            <img :src="factory.pathToLogo" alt="Factory Logo" class="img-thumbnail" style="max-width: 80px;">
          </td>
          <td>{{ factory.name }}</td>
          <td>{{ factory.location.address }}</td> <!-- Prikazujemo adresu iz podataka o lokaciji -->
          <td style="padding-left: 50px;">{{ factory.rate }}</td>
          <td>
            <button @click="pregledajFabriku(factory)" class="btn btn-outline-primary btn-sm">
              Pregledaj fabriku
            </button>
          </td>
        </tr>
        <tr v-if="sortedFactories.length === 0">
          <td colspan="5" class="text-center">Nema rezultata pretrage</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

const title = ref("Fabrike čokolade");

const searchParams = reactive({
  factoryName: '',
  chocolateName: '',
  address: '',
  averageRating: null,
  chocolateType: '',
  chocolateCategory: '',
  isOpen: false
});

const sortParams = reactive({
  criterion: '',
  ascending: 'true'
});

const factories = ref([]);
const filteredFactories = ref([]);

const chocolateTypeFilters = reactive({
  milk: false,
  dark: false,
  white: false
});

const chocolateCategoryFilters = reactive({
  regular: false,
  cooking: false
});

onMounted(async () => {
  await loadFactories();
});

const loadFactories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/WebShopAppREST/rest/factories');
    factories.value = response.data;
    console.log(factories);
    filterFactories();
  } catch (error) {
    console.error(error);
    title.value = "Greška u učitavanju";
  }
};

const filterFactories = () => {
  filteredFactories.value = factories.value.filter(factory => {
    const matchFactoryName = factory.name.toLowerCase().includes(searchParams.factoryName.toLowerCase());
    const matchChocolateName = factoryContainsChocolate(factory, searchParams.chocolateName);
    const matchAddress = factory.location.address.toLowerCase().includes(searchParams.address.toLowerCase());
    const matchAverageRating = !searchParams.averageRating || factory.rate >= searchParams.averageRating;
    const matchChocolateCategory =
      (!chocolateCategoryFilters.regular || factoryContainsChocolateWithCategory(factory, 'Regular')) &&
      (!chocolateCategoryFilters.cooking || factoryContainsChocolateWithCategory(factory, 'Cooking'));
    const matchIsOpen = !searchParams.isOpen || factory.status == "Work";
    const matchChocolateType =
      (!chocolateTypeFilters.milk || factoryContainsChocolateWithType(factory, 'Milk')) &&
      (!chocolateTypeFilters.dark || factoryContainsChocolateWithType(factory, 'Dark')) &&
      (!chocolateTypeFilters.white || factoryContainsChocolateWithType(factory, 'White'));

    return (
      matchFactoryName &&
      matchChocolateName &&
      matchAddress &&
      matchAverageRating &&
      matchChocolateCategory &&
      matchIsOpen &&
      matchChocolateType
    );
  });
};

const factoryContainsChocolate = (factory, chocolateName) => {
  // Implement logic to check if factory contains chocolate with given chocolateName
  return true; // Replace with actual implementation
};

const factoryContainsChocolateWithType = (factory, chocolateType) => {
  // Implement logic to check if factory contains chocolate with given chocolateType
  return true; // Replace with actual implementation
};

const factoryContainsChocolateWithCategory = (factory, chocolateCategory) => {
  // Implement logic to check if factory contains chocolate with given chocolateCategory
  return true; // Replace with actual implementation
};

const pregledajFabriku = (factory) => {
  router.push({ path: `/factoriesAll/${factory.id}` });
};

const searchFactories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/WebShopAppREST/rest/factories/search', {
      params: {
        factoryName: searchParams.factoryName,
        chocolateName: searchParams.chocolateName,
        address: searchParams.address,
        averageRating: searchParams.averageRating,
        chocolateType: searchParams.chocolateType,
        chocolateCategory: searchParams.chocolateCategory,
        isOpen: searchParams.isOpen
      }
    });
    factories.value = response.data;
    filterFactories();
  } catch (error) {
    console.error(error);
    title.value = "Greška u pretrazi";
  }
};

const sortedFactories = computed(() => {
  let sorted = [...filteredFactories.value];
  if (sortParams.criterion) {
    sorted.sort((a, b) => {
      let modifier = sortParams.ascending === 'true' ? 1 : -1;
      if (sortParams.criterion === 'address') {
        // Comparing addresses directly
        let addressA = a.location.address.toLowerCase();
        let addressB = b.location.address.toLowerCase();
        if (addressA < addressB) return -1 * modifier;
        if (addressA > addressB) return 1 * modifier;
        return 0;
      } else {
        // For other criteria (name, rate), compare directly
        if (a[sortParams.criterion] < b[sortParams.criterion]) return -1 * modifier;
        if (a[sortParams.criterion] > b[sortParams.criterion]) return 1 * modifier;
        return 0;
      }
    });
  }
  return sorted;
});

const updateChocolateTypeFilters = () => {
  searchParams.chocolateType = Object.keys(chocolateTypeFilters).filter(key => chocolateTypeFilters[key]).join(',');
  filterFactories();
};

const updateChocolateCategoryFilters = () => {
  searchParams.chocolateCategory = Object.keys(chocolateCategoryFilters).filter(key => chocolateCategoryFilters[key]).join(',');
  filterFactories();
};
</script>

<style>
.table td {
    vertical-align: middle;
}
.table-container {
  height: 500px; /* Promenite visinu po potrebi */
  overflow-y: auto; /* Omogućava vertikalni scroll */
}
</style>