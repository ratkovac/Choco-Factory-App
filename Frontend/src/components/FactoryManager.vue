<template>
      <div class="container-fluid">
    <div v-if="show">
      <aside>
      </aside>
      <input type="checkbox" id="menu-toggle"/>
      <label id="trigger" for="menu-toggle"></label>
      <label id="burger" for="menu-toggle"></label>
      <ul id="menu">
        <li><a href="#" @click="factoryShowClick">Your Factory</a></li>
        <li><a href="#" @click="purchasesDisplayClick">Purchases</a></li>
        <li><a href="#" @click="addWorker">New Worker</a></li>
        <li><a href="#" @click="yourProfile">Your Profile</a></li>
        <li><a href="#" @click="commentsClick">Comments</a></li>
        <li><a href="#" @click="logout">Logout</a></li>
      </ul>
    </div>
    <div v-if="showFactory">
        <div class="col-md-3">
        <img :src="factory.pathToLogo" alt="Factory Logo" style="max-width: 100px;">
        </div>
        <div class="col-md-50">
        <h2><strong>Naziv:</strong> {{ factory.name }}</h2>
        <p><strong>Radno vreme:</strong> {{ factory.workingTime }}</p>
        <p><strong>Lokacija:</strong> {{ factory.location }}</p>
        <p v-if="factory.rate"><strong>Ocena:</strong> {{ factory.rate }}</p>
        <p v-if="factory.status"><strong>Status:</strong> {{ factory.status }}</p>
        <p><strong>Komentari:</strong>
            <ul v-if="comments && comments.length > 0">
            <div v-for="comment in comments" :key="comment.id">{{ comment.text }}</div>
            </ul>
            <span v-else>Nema komentara</span>
        </p>
        <p><strong>Cokolade:</strong>
            <span v-if="!(chocolates && chocolates.length > 0)"> Nema dostupnih čokolada</span>
        </p>
        <div class="table-responsive" v-if="chocolates && chocolates.length > 0">
            <table v-if="chocolates && chocolates.length > 0" class="custom-table">
                <thead>
                    <tr style="background-color: #573b8a; border-color: #ccc; color: white;">
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px;">Slika</th>
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px;">Naziv</th>
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px;">Cena</th>
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px;">Kategorija</th>
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px;">Tip</th>
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px;">Masa</th>
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px;">Detalji</th>
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px;">Zaliha</th>
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px;"></th> <!-- Dodajte praznu ćeliju za dugmad -->
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
                            <button @click="updateChocolate(chocolate.id, factory)" style="width: 130px; margin-bottom: 0px; margin-right: 30px; background-color: #573b8a; border-color: #ccc; font-weight: 800; font-size: 14px; letter-spacing: 1px;" class="btn btn-primary btn-add-to-cart">Ažuriraj</button>
                            <button @click="deleteChocolate(chocolate.id, factory)" style="width: 100px; margin-bottom: 0px; margin-right: 30px; background-color: #573b8a; border-color: #ccc; font-weight: 800; font-size: 14px; letter-spacing: 1px;" class="btn btn-primary btn-add-to-cart">Obriši</button>
                        </td>
                    </tr>
                </tbody>
            </table>
            <span v-else>Nema dostupnih čokolada</span>
        </div>

        <button @click="addChcolate(factory)" style="width: 200px; margin-bottom: 0px; margin-right: 30px; background-color: #573b8a; border-color: #ccc; font-weight: 800; font-size: 17px; letter-spacing: 1px;" class="btn btn-primary btn-add-to-cart">
            Dodaj cokoladu
            </button>

        <button @click="addWorker()" style="width: 160px; margin-bottom: 0px; margin-right: 30px; background-color: #573b8a; border-color: #ccc; font-weight: 800; font-size: 17px; letter-spacing: 1px;" class="btn btn-primary btn-add-to-cart">Add Worker</button>
        </div>
    </div>
    <div v-else-if="showNewWorkerForm">
        <!-- Vaš HTML za selekciju menadžera -->
    <!-- New Manager Fields -->
    <div class="form-group">
        <label for="username">Username</label>
        <input id="username" type="text" v-model="newManagerForm.username" required>
    </div>
    
    <div class="form-group">
        <label for="password">Password</label>
        <input id="password" type="password" v-model="newManagerForm.password" required>
    </div>
    
    <div class="form-group">
        <label for="confirmPassword">Confirm Password</label>
        <input id="confirmPassword" type="password" required>
    </div>
    
    <div class="form-group">
        <label for="firstName">First Name</label>
        <input id="firstName" type="text" v-model="newManagerForm.name" required>
    </div>
    
    <div class="form-group">
        <label for="lastName">Last Name</label>
        <input id="lastName" type="text" v-model="newManagerForm.surname" required>
    </div>
    
    <div class="form-group">
        <label for="dob">Date of Birth</label>
        <input id="dob" type="date" v-model="newManagerForm.birthDate" required>
    </div>
    
    <div class="form-group">
        <label for="gender">Gender</label>
        <select id="gender" v-model="newManagerForm.gender" required>
            <option value="male">Male</option>
            <option value="female">Female</option>
        </select>
    </div>
    <!-- Back to Manager Selection and Create Manager Buttons -->
    <div class="form-group buttons-inline" style="margin-top: 40px; margin-bottom: 0px">
        <button type="button" style="width: 300px; margin-bottom: 0px; margin-left: 20px; background-color: #573b8a; border-color: #ccc; font-weight: 800; font-size: 16px; letter-spacing: 1px;" class="btn btn-primary btn-add-to-cart" @click="createWorker" >Add Worker</button>
    </div> 
    </div>
    </div>

    <!-- USER PROFILE -->
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
                  <button type="button" style="width: 160px; margin-bottom: 0px; margin-right: 30px; background-color: #573b8a; border-color: #ccc; font-weight: 800; font-size: 14px; letter-spacing: 1px;" class="btn btn-primary btn-add-to-cart" @click="saveProfile">
                    Save Profile
                  </button>
                </div>
                <div class="col-md-6">
                  <button style="width: 160px; margin-bottom: 0px; margin-right: 30px; background-color: #573b8a; border-color: #ccc; font-weight: 800; font-size: 14px; letter-spacing: 1px;" class="btn btn-primary btn-add-to-cart" type="button" @click="logout">Logout</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

 <!-- KOMENTARI -->
  <div v-if="commentView">
    <!-- Prikaz komentara -->
    <div class="mt-4">
        <label style="font-weight: 900; font-size: 24px; margin-left: 10px; letter-spacing: 1px; color: #573b8a;">Komentari</label>
        <div class="table-responsive" v-if="comments && comments.length > 0">
            <table class="custom-table">
                <thead class="table-dark">
                    <tr style="background-color: #573b8a; border-color: #ccc; color: white;">
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px; color: white;">Tekst komentara</th>
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px; color: white;">Status</th>
                        <th class="border" style="font-weight: 700; font-size: 14px; letter-spacing: 1px; color: white;"></th> <!-- Kolona za dugmad -->
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="comment in comments" :key="comment.id">
                        <td>{{ comment.text }}</td>
                        <td>{{ comment.valid ? 'Validan' : 'Nevalidan' }}</td>
                        <td v-if="!comment.valid">
                            <button @click="approveComment(comment.id)" class="btn btn-success btn-sm">Odobri</button>
                            <button @click="rejectComment(comment.id)" class="btn btn-danger btn-sm">Odbij</button>
                        </td>
                        <td v-if="comment.valid">
                            <button @click="rejectComment(comment.id)" class="btn btn-danger btn-sm">Odbij</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <span v-else>Nema komentara</span>
    </div>
    </div>
  <div v-if="purchasesShow" class="purchases-section">
    <h2>Kupovine u vašoj fabrici:</h2>
    <div class="card-container">
      <div v-for="purchase in purchases" :key="purchase.id" class="card">
        <div class="card-body">
          <h5 class="card-title">{{ purchase.factory.name }}</h5>
          <p class="card-text"><strong>Cena:</strong> {{ purchase.price }} RSD</p>
          <p class="card-text"><strong>Status:</strong> {{ purchase.status }}</p>
          <p class="card-text"><strong>Datum:</strong> {{ purchase.purchaseDateTime }}</p>
        </div>
      </div>
      <div v-if="purchases.length === 0" class="no-results">
        Nema rezultata pretrage
      </div>
    </div>
  </div>
  <!-- <div v-if="purchasesShow" class="purchases-section">
      <h2>Kupovine u vašoj fabrici:</h2>
      <div class="purchases-list row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4" style="margin-top: 10px;">
              <div class="col" v-for="purchase in purchases" :key="purchase.id">
                <div class="card h-100">
                  <img :src="purchase.factory.pathToLogo" class="card-img-top img-thumbnail" alt="Slika fabrike">
                  <div class="card-body">
                    <h5 class="card-title">{{ purchase.factory.name }}</h5>
                    <p class="card-text">Cena: {{ purchase.price }} RSD</p>
                    <p class="card-text">Status: {{ purchase.status }}</p>
                    <p class="card-text">Datum: {{ purchase.purchaseDateTime }}</p>
                  </div>
                </div>
              </div>
            </div>
      <div class="table-responsive" style="min-height: 500px">
    <table class="table table-hover">
      <thead class="table-dark">
        <tr>
          <th scope="col">Naziv fabrike</th>
          <th scope="col">Cena</th>
          <th scope="col">Status</th>
          <th scope="col">Datum</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="purchase in purchases" :key="purchase.id">
          <td>{{ purchase.factory.name }}</td>
          <td>{{ purchase.price }}</td>
          <td>{{ purchase.status }}</td>
          <td>{{ purchase.purchaseDateTime }}</td>
        </tr>
        <tr v-if="purchases.length === 0">
          <td colspan="5" class="text-center">Nema rezultata pretrage</td>
        </tr>
      </tbody>
    </table>
  </div>
    </div> -->
  </template>
  
  <script setup>
  import { ref, onMounted, computed, reactive } from 'vue';
  import axios from 'axios';
  import { useRoute, useRouter } from 'vue-router';

  const route = useRoute()
  const router = useRouter()

  const show = ref(true);
  const showFactory = ref(true);
  const showNewWorkerForm = ref(false);
  const profile = ref(false);
  const commentView = ref(false);
  const purchasesShow = ref(false);
  const purchases = ref([]);

  const firstName = ref(route.query.firstName);
const lastName = ref(route.query.lastName);
const username = ref(route.query.username);

  const factory = ref({
  name: '',
  location: '',
  pathToLogo: '',
  rate: 0,
  isDeleted: false,
  workingTime: ''
  });
  const newManagerForm = ref({
  username: '',
  password: '',
  name: '',
  surname: '',
  gender: '',
  birthDate: ''
});
  const chocolates = ref([]);
  const comments = ref([]);

  const addWorker = () => {
    showFactory.value = false;
    showNewWorkerForm.value = true;
    profile.value = false;
    purchasesShow.value = false;
  };
  
  const commentsClick = () => {
    showFactory.value = false;
    showNewWorkerForm.value = false;
    profile.value = false;
    commentView.value = true;
  }
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

  const purchasesDisplayClick = () => {
  loadPurchases();
  purchasesShow.value = true;
  showFactory.value = false;
  profile.value = false;
  showNewWorkerForm.value = false;
  console.log("purchasesShow:", purchasesShow.value);
};

const factoryShowClick = () => {
  showFactory.value = true;
  purchasesShow.value = false;
  showNewWorkerForm.value = false;
  profile.value = false;
}

const loadPurchases = async () => {
  try {
    const factoryId = route.params.id;
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/purchases/factory/${factoryId}`);
    purchases.value = response.data;

    for (let purchase of purchases.value) {
      const factoryId = purchase.factory.id;
      const factoryResponse = await axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/${factoryId}`);
      purchase.factory.name = factoryResponse.data.name; // Dodaj naziv fabrike u objekat kupovine
    }
  } catch (error) {
    console.error(error);
  }
};
  
  onMounted(async () => {
  try {
    const factoryId = route.params.id; 
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/${factoryId}`);
    factory.value = response.data;
    chocolates.value = await getChocolatesByFactory(factoryId);
    comments.value = await getCommentsByFactory(factoryId);
    console.log(comments);
    firstName.value = route.query.firstName;
    lastName.value = route.query.lastName;
    username.value = route.query.username;
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

  const approveComment = async (commentId) => {
    try {
        const response = await axios.put(`http://localhost:8080/WebShopAppREST/rest/comments/accept/${commentId}`);
        console.log(response);
        comments.value = await getCommentsByFactory(factoryId);
    } catch (error) {
        console.log(error);
    }
  }
  const rejectComment = async (commentId) => {
    try {
        const response = await axios.put(`http://localhost:8080/WebShopAppREST/rest/comments/reject/${commentId}`);
        console.log(response);
    } catch (error) {
        console.log(error);
    }
  }
  const deleteChocolate = async (chocolateId, factory) => {
    try {
      const response = await axios.delete(`http://localhost:8080/WebShopAppREST/rest/chocolates/${chocolateId}`); 
      chocolates.value = await getChocolatesByFactory(factory.id);
    } catch (error) {
      console.error(error);
    }
  };

  const createWorker = async () => {
  try {
    const response = await axios.post('http://localhost:8080/WebShopAppREST/rest/user/register/w', newManagerForm.value);
    if (!response.data) {
      console.error("Manager wasn't able to be created! Error");
    } else {        
      console.log(response.data);
      showFactory.value = true;
    showNewWorkerForm.value = false;
    }
  } catch (error) {
    console.error(error);
    console.error("Manager wasn't able to be created! Error");
  }
};

// PROFIL //

const yourProfile = () => {
  profile.value = true;
  showFactory.value = false;
  showNewWorkerForm.value = false;
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
  profileImageUrl: ""
}); // Definicija na globalnoj razini

const fetchUser = () => {
  const userId = route.query.id; // Pretpostavlja se da se koristi ID iz rute
  console.log(userId);
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
  
  <style scoped>
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

.no-results {
  text-align: center;
  width: 100%;
  margin-top: 20px;
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

  .btn-view-factory {
    background-color: #573b8a;
    border-color: #411594;
    height: 50px;
    width: auto;
    font-size: 15px;
    font-size: 16px;
    color: white;
  }
  .btn-update {
    background-color: #573b8a;
    border-color: #3e1786;
    height: 25px;
    width: auto;
    font-size: 12px;
    color: white;
  }
  .main {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: auto;
    background-color: #f0f0f0; /* Light gray background */
    border: 10px solid #6d44b8; /* Border with increased thickness */
    border-radius: 15px; /* Adjust border radius for rounded corners */
}

.signup {
    background-color: #fff; /* White background for the form */
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Light shadow */
    width: 400px; /* Adjust width as needed */
}

.form-group {
    margin-bottom: 20px; /* Spacing between form groups */
}

label {
    font-weight: bold;
    color: #333; /* Darker font color */
}

input[type="text"],
input[type="password"],
input[type="date"],
select {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc; /* Light gray border */
    border-radius: 4px;
    box-sizing: border-box; /* Ensure padding and border are included in width */
}

select {
    height: 40px; /* Match input height for consistency */
}

.logo-upload {
    display: flex;
    align-items: center;
}

.selected-logo {
    max-width: 80px; /* Adjust size of selected logo */
    max-height: 50px;
    margin-left: 10px; /* Margin to separate from file input */
}

.buttons-inline {
    display: flex;
    justify-content: flex-start; /* Align buttons to the start */
    gap: 30px; /* Adjust space between buttons */
}

.create-factory-button {
    padding: 10px;
    background-color: #573b8a; /* Purple background */
    color: #fff; /* White text */
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s ease; /* Smooth transition */
}

.create-factory-button:hover {
    background-color: #6d44b8; /* Darker purple on hover */
}

.add-manager-button {
    width: 80%;
    height: 40px;
    margin-left: 40px;
    padding: 10px;
    background-color: white; /* White background */
    color: #6d44b8; /* Purple text color */
    border: 3.5px solid #6d44b8;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s ease; /* Smooth transition */
}

.add-manager-button:hover {
    background-color: #6d44b8; /* Darker purple on hover */
    color: white; /* White text color */
}
@import url(https://fonts.googleapis.com/css?family=Roboto:400,700);

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
    background-color: #573b8a;
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
  