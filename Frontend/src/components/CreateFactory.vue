<template>
    <div>
    <input type="checkbox" id="menu-toggle"/>
    <label id="trigger" for="menu-toggle"></label>
    <label id="burger" for="menu-toggle"></label>
    <ul id="menu">
      <li><a href="#" @click="factoryShowClick">Factories</a></li>
      <li><a href="#" @click="dodajFabriku">New Factory</a></li>
      <li><a href="#" @click="yourProfile">Your Profile</a></li>
      <li><a href="#" @click="logout">Log out</a></li>
    </ul>
  </div>
    <div class="main">
        <div class="signup">
            <form @submit.prevent="submitFactory" v-if="!showManagerSelection && !showNewManagerForm">
                <label style="font-size: 24px; margin-left: 100px;" for="chk" aria-hidden="true">Create Factory</label>
                
                <!-- Factory Name -->
                <div class="form-group">
                    <label for="factoryName">Factory Name</label>
                    <input id="factoryName" type="text" v-model="factoryForm.name" required>
                </div>
                
                 <!-- Location with Map Component 
                 <div class="form-group">
                    <label for="location">Location</label>
                    <MapComponent @update-location="updateLocation" :location="factoryForm.location"></MapComponent>
                </div> -->
                <div class="form-group">
                    <label for="location">Location</label>
                    <input id="location" type="text" v-model="factoryForm.location" required>
                </div>

                
                <!-- Working Hours -->
                <div class="form-group">
                    <label for="workingHours">Working Hours</label>
                    <input id="workingHours" type="text" v-model="factoryForm.workingTime" required>
                </div>
                
                <!-- Logo URL -->
                <div class="form-group">
                    <label for="logoUrl">Logo URL</label>
                    <input id="logoUrl" type="text" v-model="factoryForm.pathToLogo" required>
                    <!-- Show selected logo -->
                    <img v-if="factoryForm.pathToLogo" :src="factoryForm.pathToLogo" alt="Selected Logo" class="selected-logo">
                </div>
                
                <!-- Choose Manager Button -->
                <div class="form-group">
                    <button type="button" class="add-manager-button" @click="toggleManagerSelection">Choose Manager</button>
                </div>
            </form>
            
            <!-- Manager Selection -->
            <div v-else-if="showManagerSelection">
                <label style="font-size: 24px; margin-left: 100px;" for="chk" aria-hidden="true">Choose Manager</label>
                
                <!-- Manager Selection -->
                <div class="form-group" style="margin-bottom: 5px;">
                    <label for="managers">Select Manager</label>
                    <select id="managers" v-model="selectedManager">
                        <option v-for="manager in availableManagers" :key="manager.id" :value="manager.id">
                            {{ manager.firstName }} {{ manager.lastName }}
                        </option>
                    </select>
                </div>
                <div>
                    <label style="margin-left: 170px;"> OR </label>
                </div>
                <!-- Add New Manager Button -->
                <div class="form-group" style="margin-top: 5px;">
                    <button type="button" class="add-manager-button" @click="addNewManager" style="margin-left: 0px; width: 360px;">Add New Manager</button>
                </div>             
                
                <!-- Back to Factory Form Button -->
                <div class="form-group buttons-inline" style="margin-top: 40px; margin-bottom: 0px">
                    <button type="button" class="create-factory-button" @click="toggleManagerSelection" style="width: 300px;">Back to Factory Form</button>
                    <button type="submit" class="create-factory-button" @click="submitFactory"style="width: 250px;">Create Factory</button>
                </div>
            </div>
            
            <!-- New Manager Form -->
            <div v-else-if="showNewManagerForm">
                <label style="font-size: 24px; margin-left: 100px;" for="chk" aria-hidden="true">Add New Manager</label>
                
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
                    <button type="button" class="create-factory-button" @click="createNewManager" style="width: 170px; height: 40px;">Create New Manager</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import MapComponent from './MapComponent.vue';

const router = useRouter();

const factoryForm = ref({
  name: '',
  location: '',
  workingTime: '',
  pathToLogo: '', 
  rate: 0.0,
  isDeleted: false,
  comments: [],
  status: 'Ne radi',
  chocolateIds: []
});

const availableManagers = ref([]);

onMounted(async () => {
    try {
        const response = await axios.get('http://localhost:8080/WebShopAppREST/rest/user/allManagers');
        availableManagers.value = response.data;
    } catch (error) {
        console.error('Error fetching managers:', error);
    }
});

const selectedManager = ref(null);
const showManagerSelection = ref(false);
const showNewManagerForm = ref(false);

const newManagerForm = ref({
  username: '',
  password: '',
  name: '',
  surname: '',
  gender: '',
  birthDate: ''
});

const submitFactory = async () => {
  try {
    // POST request to add factory
    const response = await axios.post('http://localhost:8080/WebShopAppREST/rest/factories/add', factoryForm.value);
    if (!response.data) {
      console.error("Factory wasn't able to be saved! Error");
    } else {
      console.log(response.data);
      
      // Save the factory ID from the response
      const factoryId = response.data.id;
      const selectedManagerId = selectedManager.value; // Use only the ID
      
      console.log("Factory ID:", factoryId);
      console.log("Selected Manager ID:", selectedManagerId);
      
      // Use the factoryId in a PUT request to update manager
      const updateResponse = await axios.put(
      `http://localhost:8080/WebShopAppREST/rest/user/update/manager/${factoryId}`,
      null, // Tijelo zahtjeva je null jer se managerId šalje kao query parametar
      {
        params: {
          managerId: selectedManagerId
        },
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

      console.log(updateResponse.data); // Handle the response as needed
    }
  } catch (error) {
    console.error(error);
    console.error("Factory or manager update failed! Error");
  }
};



const toggleManagerSelection = () => {
  showManagerSelection.value = !showManagerSelection.value;
  showNewManagerForm.value = false;
};

const addNewManager = () => {
  showManagerSelection.value = false;
  showNewManagerForm.value = true;
};

const createNewManager = async () => {
  try {
    const response = await axios.post('http://localhost:8080/WebShopAppREST/rest/user/register/m', newManagerForm.value);
    if (!response.data) {
      console.error("Manager wasn't able to be created! Error");
    } else {
        try {
            const response = await axios.get('http://localhost:8080/WebShopAppREST/rest/user/allManagers');
            availableManagers.value = response.data;
        } catch (error) {
            console.error('Error fetching managers:', error);
        }
      console.log(response.data);
      availableManagers.value.push(response.data);
      showNewManagerForm.value = false;
      showManagerSelection.value = true;
    }
  } catch (error) {
    console.error(error);
    console.error("Manager wasn't able to be created! Error");
  }
};
</script>

<style scoped>
.main {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: auto;
    background-color: #f0f0f0; /* Light gray background */
    border: 10px solid #2E8B57; /* Border with increased thickness */
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
    background-color: #2E8B57; /* Purple background */
    color: #fff; /* White text */
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s ease; /* Smooth transition */
}

.create-factory-button:hover {
    background-color: #2E8B57; /* Darker purple on hover */
}

.add-manager-button {
    width: 80%;
    height: 40px;
    margin-left: 40px;
    padding: 10px;
    background-color: white; /* White background */
    color: #2E8B57; /* Purple text color */
    border: 3.5px solid #2E8B57;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s ease; /* Smooth transition */
}

.add-manager-button:hover {
    background-color: #2E8B57; /* Darker purple on hover */
    color: white; /* White text color */
}

</style>
