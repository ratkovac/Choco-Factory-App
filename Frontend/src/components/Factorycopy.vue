<template>
  <div>
    <input type="checkbox" id="menu-toggle"/>
    <label id="trigger" for="menu-toggle"></label>
    <label id="burger" for="menu-toggle"></label>
    <ul id="menu">
      <li><a href="#" @click="factoryShowClick">Factories</a></li>
      <li><a href="#" @click="dodajFabriku">New Factory</a></li>
      <li><a href="#" @click="yourProfile">Your Profile</a></li>
      <li><a href="#" @click="allUsers">Users</a></li>
      <li><a href="#" @click="logout">Log out</a></li>
    </ul>
  </div>

  <div v-if="factoriesView">
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
      <input v-model="searchParams.location" type="text" class="form-control" placeholder="Lokacija" @input="filterFactories">
    </div>
    <div class="col-md-2 mb-2">
      <input v-model="searchParams.averageRating" type="number" class="form-control" placeholder="Prosečna ocena" @input="filterFactories">
    </div>
    <div class="col-md-2 mb-2">
      <select v-model="sortParams.criterion" class="form-select" aria-label="Default select example">
        <option value="">Sortiraj po...</option>
        <option value="name">Naziv</option>
        <option value="address">Lokacija</option>
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
            <h6 class="fw-bold">Category:</h6>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" v-model="chocolateCategoryFilters.regular" @change="updateChocolateCategoryFilters">
              <label class="form-check-label">
                Regular
              </label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" v-model="chocolateCategoryFilters.cooking" @change="updateChocolateCategoryFilters">
              <label class="form-check-label">
                Cooking
              </label>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div>
            <h6 class="fw-bold">Type:</h6>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" v-model="chocolateTypeFilters.milk" @change="updateChocolateTypeFilters">
              <label class="form-check-label">
                Milk
              </label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" v-model="chocolateTypeFilters.dark" @change="updateChocolateTypeFilters">
              <label class="form-check-label">
                Dark
              </label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" v-model="chocolateTypeFilters.white" @change="updateChocolateTypeFilters">
              <label class="form-check-label">
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
  <div class="table-responsive" style="min-height: 500px">
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
          <td>{{ factory.location.address }}</td>
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
</div>

  <div v-if="profile">
    <div>
    <div>
      <div class="container rounded bg-white mt-5 mb-5">
        <div class="row justify-content-center">
          <div class="col-md-4 border-right">
            <div class="d-flex flex-column align-items-center text-center p-3 py-5">
              <img
                class="rounded-circle mt-5"
                width="150px"
                :src="user.profileImageUrl ? user.profileImageUrl : 'https://static.vecteezy.com/system/resources/previews/036/280/650/original/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-illustration-vector.jpg'"
                alt="User Profile Image"
              />
              <span class="font-weight-bold">{{ user.username }}</span>
            </div>
          </div>
          <div class="col-md-5 border-right">
            <div class="p-3 py-5">
              <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="text-right">Profile Settings</h4>
              </div>
              <div class="row mt-2">
                <div class="col-md-6">
                  <label class="labels">Name</label>
                  <input type="text" class="form-control" v-model="user.firstName" />
                </div>
                <div class="col-md-6">
                  <label class="labels">Surname</label>
                  <input type="text" class="form-control" v-model="user.lastName" />
                </div>
              </div>
              <div class="row mt-3">
                <div class="col-md-12">
                  <label class="labels">Username</label>
                  <input type="text" class="form-control" v-model="user.username" />
                </div>
                <div class="col-md-12">
                  <label class="labels">Gender</label>
                  <input type="text" class="form-control" v-model="user.gender" />
                </div>
                <div class="col-md-12">
                  <label class="labels">Birth date</label>
                  <input type="text" class="form-control" v-model="user.birthDate" />
                </div>
                <div class="col-md-12">
                  <label class="labels">Role</label>
                  <input type="text" class="form-control" v-model="user.role" />
                </div>
              </div>
              <div class="row mt-4">
                <div class="col-md-6">
                  <button style="width: 160px; margin-bottom: 0px; margin-right: 30px; background-color: #2E8B57; border-color: #ccc; font-weight: 800; font-size: 14px; letter-spacing: 1px;" class="btn btn-primary btn-add-to-cart" type="button" @click="saveProfile">
                    Save Profile
                  </button>
                </div>
                <div class="col-md-6">
                  <button style="width: 160px; margin-bottom: 0px; margin-right: 30px; background-color: #2E8B57; border-color: #ccc; font-weight: 800; font-size: 14px; letter-spacing: 1px;" class="btn btn-primary btn-add-to-cart" type="button" @click="logout">Logout</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();

const title = ref("Fabrike čokolade");
const factoriesView = ref(true);
const newManagerView = ref(false);
const profile = ref(false);

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

const dodajFabriku = () => {
  router.push({ path: `/factories/admin/create` })
}
const allUsers = () => {
  router.push({ path: `/users`});
}
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
  router.push({ path: `/factories/${factory.id}` });
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

const factoryShowClick = () => {
    profile.value = false;
    factoriesView.value = true;
    newManagerView.value = false;
}
  // PROFIL //
  const yourProfile = () => {
    profile.value = true;
    factoriesView.value = false;
    newManagerView.value = false;
    fetchUser();
  };
  const user = ref({
  id: "",
  firstName: "",
  lastName: "",
  username: "",
  password: "",
  gender: "",
  birthDate: "",
  role: "",
  isActive: "",
  status: "",
  profileImageUrl: "",
  type: ""
}); // Definicija na globalnoj razini

const fetchUser = () => {
  console.log("EEEEE");
  const userId = route.query.id; // Pretpostavlja se da se koristi ID iz rute
  console.log(route.params.id);
  axios
    .get(`http://localhost:8080/WebShopAppREST/rest/user/profile/${userId}`)
    .then(response => {
      user.value = response.data; // Postavljanje vrijednosti korisnika
      console.log('Pronađen korisnik:', user);
    })
    .catch(error => {
      console.error('Greška prilikom dohvaćanja korisnika:', error);
    });
};


const saveProfile = async () => {
  console.log('Ažuriranje korisnika...');
  try {
    const response = await axios.put(`http://localhost:8080/WebShopAppREST/rest/user/update/${user.value.id}`, user.value);
    console.log('Korisnik uspješno ažuriran');
    localStorage.setItem('user', JSON.stringify(user.value)); // Ažurirajte lokalno skladište s novim podacima korisnika
  } catch (error) {
    console.error('Došlo je do greške:', error);
  }
};

const logout = () => {
  localStorage.removeItem('user');
  router.push('/login');
};
</script>

<style>
.table td {
    vertical-align: middle;
}
/* @import url(https://fonts.googleapis.com/css?family=Roboto:400,700); */

@keyframes checked-anim {
  50% {
    width: 300px; /* Smanjite širinu animacije */
    height: 450px; /* Smanjite visinu animacije */
  }
  100% {
    width: 250px; /* Konačna širina menija */
    height: 400px; /* Konačna visina menija */
    border-bottom-right-radius:20%;
  }
}
@keyframes not-checked-anim {
    0% {
        width: 3000px;
        height: 3000px;
    }
}
li, a {
    margin: 75px 0 -55px 0;
    color: #fff;
    font: 14pt "Roboto", sans-serif;
    font-weight: 700;
    line-height: 1.8;
    text-decoration: none;
    text-transform: none;
    list-style: none;
    outline: 0;
    display: none;
}
li {
    width: 230px;
    text-indent: 56px;}
a:focus {
    display: block;
    color: #333;
    background-color: #eee;
    transition: all .5s;
}
aside {
    position: absolute;
    color: white;
    top: 35%;
    right: 10%;
    text-align: right;
}
h1 {
    line-height: 0;
    font-size: 4vw;
    font-weight: 700;
}
h3 {
    float: right;
    line-height: .3;
    font-size: 2.5vw;
    font-weight: lighter;
}
h4 {
    float: left;
    margin-left: -2%;
    font-size: 1.5vw;
    font-weight: lighter;
}

html, body, template{
    margin: 0;
    padding: 0;
    height: 100%;
    background-color: #fff;
    font-family: 'Roboto', sans-serif;
    overflow: hidden;
}

#trigger, #burger, #burger:before, #burger:after {
    position: absolute;
    top: 25px;
    left: 25px; /* Align the right edge */
    background: #fff;
    width: 30px;
    height: 5px;
    transition: .2s ease;
    cursor: pointer;
    z-index: 1;
}

#trigger {
    height: 25px;
    background: none;
}
#burger:before {
    content: " ";
    top: 10px;
    left: 0;
}
#burger:after {
    content: " ";
    top: 20px;
    left: 0;
}
#menu-toggle:checked + #trigger + #burger {
    top: 35px;
    transform: rotate(180deg);
    transition: transform .2s ease;
}

#menu-toggle:checked + #trigger + #burger:before {
    width: 20px;
    top: -2px;
    left: 18px;
    transform: rotate(45deg) translateX(-5px);
    transition: transform .2s ease;
}
#menu-toggle:checked + #trigger + #burger:after {
    width: 20px;
    top: 2px;
    left: 18px;
    transform: rotate(-45deg) translateX(-5px);
    transition: transform .2s ease;
}
#menu {
    position: absolute;
    top: 0; /* Align the top edge */
    left: 0; /* Align the right edge */
    margin: 0; 
    padding: 0;
    width: 110px;
    height: 110px;
    background-color: #2E8B57;
    border-bottom-right-radius: 100%;
    box-shadow: 0 2px 5px rgba(0,0,0,0.26);
    animation: not-checked-anim .2s both;
    transition: .2s;
    overflow: hidden; /* Dodajte overflow hidden */
}

#menu-toggle:checked + #trigger + #burger + #menu {
    animation: checked-anim 1s ease both;
}
#menu-toggle:checked + #trigger ~ #menu > li, a {
    display: block;
}
[type="checkbox"]:not(:checked), [type="checkbox"]:checked {
    display: none;
}
</style>