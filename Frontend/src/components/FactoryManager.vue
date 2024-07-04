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
        <li><a href="#" @click="addWorker">New Worker</a></li>
        <li><a href="#">Your Profile</a></li>
        <li><a href="#">Logout</a></li>
      </ul>
    </div>
    <div v-if="showFactory">
        <h2><label>Username:</label> {{  firstName }}</h2>
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

        <button @click="addWorker()" class="btn btn-primary btn-view-factory">Add Worker</button>
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
        <button type="button" class="create-factory-button" @click="toggleManagerSelection" style="width: 170px; height: 40px;">Back to Selection</button>
        <button type="button" class="create-factory-button" @click="createWorker" style="width: 170px; height: 40px;">Add Worker</button>
    </div> 
    </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import { useRoute, useRouter } from 'vue-router';

  const route = useRoute()
  const router = useRouter()

  const show = ref(true);
  const showFactory = ref(true);
  const showNewWorkerForm = ref(false);

  const firstName = route.query.firstName;
const lastName = route.query.lastName;
const username = route.query.username;

  const factoryShowClick = () => {
    showFactory.value = true;
    showNewWorkerForm.value = false;
  }

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
  };
  
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
    firstName = route.query.firstName;
    lastName = route.query.lastName;
    username = route.query.username;
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
  