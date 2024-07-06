<template>
  <!-- Search Bars -->
  <h1 class="display-5 fw-bold mb-4" style="text-align: center">{{ title }}</h1>
  <div class="row gx-5 gx-lg-1 align-items-center mb-2">
    <div class="col-md-2 mb-2">
      <input v-model="searchParams.firstName" type="text" class="form-control" placeholder="Ime" @input="filterUsers">
    </div>
    <div class="col-md-2 mb-2">
      <input v-model="searchParams.lastName" type="text" class="form-control" placeholder="Prezime" @input="filterUsers">
    </div>
    <div class="col-md-2 mb-2">
      <input v-model="searchParams.username" type="text" class="form-control" placeholder="Korisničko ime" @input="filterUsers">
    </div>
    <div class="col-md-2 mb-2">
      <select v-model="sortParams.criterion" class="form-select" aria-label="Default select example">
        <option value="">Sortiraj po...</option>
        <option value="firstName">Ime</option>
        <option value="lastName">Prezime</option>
        <option value="username">Korisničko ime</option>
        <option value="points">Poeni</option>
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
    </div>
    <div class="collapse mt-3" id="collapseExample">
      <div class="row gx-1 gx-lg-1 align-items-center mb-3">
          <div class="col-md-2 mb-2 form-check">
              <input type="checkbox" id="customer" v-model="selectedRoles.Customer" @change="filterUsers" class="form-check-input">
              <label for="customer" class="form-check-label">Customer</label>
          </div>
          <div class="col-md-2 mb-2 form-check">
              <input type="checkbox" id="manager" v-model="selectedRoles.Manager" @change="filterUsers" class="form-check-input">
              <label for="manager" class="form-check-label">Manager</label>
          </div>
          <div class="col-md-2 mb-2 form-check">
              <input type="checkbox" id="administrator" v-model="selectedRoles.Administrator" @change="filterUsers" class="form-check-input">
              <label for="administrator" class="form-check-label">Administrator</label>
          </div>
          <div class="col-md-2 mb-2 form-check">
              <input type="checkbox" id="worker" v-model="selectedRoles.Worker" @change="filterUsers" class="form-check-input">
              <label for="worker" class="form-check-label">Worker</label>
          </div>
          <div class="col-md-2 mb-2 form-check">
              <input type="checkbox" id="suspicious" v-model="isSuspiciousChecked" @change="filterUsers" class="form-check-input">
              <label for="suspicious" class="form-check-label">Sumnjiv</label>
          </div>
      </div>
    </div>
  </div>

  <!-- User Table -->
  <div class="table-responsive" style="min-height: 500px">
    <table class="table table-hover">
      <thead class="table-dark">
        <tr>
          <th scope="col">Ime</th>
          <th scope="col">Prezime</th>
          <th scope="col">Korisničko ime</th>
          <th scope="col">Poeni</th>
          <th scope="col">Uloga</th>
          <th scope="col">Status</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in sortedUsers" :key="user.id">
          <td>{{ user.firstName }}</td>
          <td>{{ user.lastName }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.points }}</td>
          <td>{{ user.role }}</td>
          <td>
            <button @click="toggleUserStatus(user)" class="btn btn-sm" :class="{'btn-success': user.status === 'ACTIVATED', 'btn-danger': user.status === 'DEACTIVATED'}">
              {{ user.status === 'ACTIVATED' ? 'Deactivate' : 'Activate' }}
            </button>
          </td>
        </tr>
        <tr v-if="sortedUsers.length === 0">
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

const title = ref("Prikaz korisnika");

const searchParams = reactive({
  firstName: '',
  lastName: '',
  username: '',
  role: ''
});

const selectedRoles = reactive({
  Customer: false,
  Manager: false,
  Administrator: false,
  Worker: false,
});

const sortParams = reactive({
  criterion: '',
  ascending: 'true'
});

const users = ref([]);
const filteredUsers = ref([]);
const isSuspiciousChecked = ref(false);

onMounted(async () => {
  await loadUsers();
});

const loadUsers = async () => {
  try {
    const response = await axios.get('http://localhost:8080/WebShopAppREST/rest/user/allUsers');
    users.value = response.data;
    filterUsers();
  } catch (error) {
    console.error(error);
    title.value = "Greška u učitavanju";
  }
};

const filterUsers = () => {
  filteredUsers.value = users.value.filter(user => {
    const matchFirstName = user.firstName.toLowerCase().includes(searchParams.firstName.toLowerCase());
    const matchLastName = user.lastName.toLowerCase().includes(searchParams.lastName.toLowerCase());
    const matchUsername = user.username.toLowerCase().includes(searchParams.username.toLowerCase());

    const matchSuspicious = isSuspiciousChecked.value ? user.canceled > 5 : true;

    const matchRole = (
    (!selectedRoles.Customer && !selectedRoles.Manager && !selectedRoles.Administrator && !selectedRoles.Worker) ||
    (selectedRoles.Customer && user.role === 'Customer') ||
    (selectedRoles.Manager && user.role === 'Manager') ||
    (selectedRoles.Administrator && user.role === 'Administrator') ||
    (selectedRoles.Worker && user.role === 'Worker')
    );

    return matchFirstName && matchLastName && matchUsername && matchRole && matchSuspicious;
  });
};

const toggleUserStatus = async (user) => {
  const newStatus = !user.isActive;
  
  try {
    const response = await axios.put(`http://localhost:8080/WebShopAppREST/rest/user/toggleStatus/${user.id}`);

    if (response.status === 200) {
      user.status = newStatus ? 'ACTIVATED' : 'DEACTIVATED';
      user.isActive = newStatus;
    }
  } catch (error) {
    console.error('Failed to change user status:', error);
  }
};

const sortedUsers = computed(() => {
  let sorted = [...filteredUsers.value];
  if (sortParams.criterion) {
    sorted.sort((a, b) => {
      let modifier = sortParams.ascending === 'true' ? 1 : -1;
      if (a[sortParams.criterion] < b[sortParams.criterion]) return -1 * modifier;
      if (a[sortParams.criterion] > b[sortParams.criterion]) return 1 * modifier;
      return 0;
    });
  }
  return sorted;
});
</script>

<style>
.table td {
    vertical-align: middle;
}
</style>
