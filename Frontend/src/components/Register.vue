<template>
  <div>
    <h2 style="color: #FFF;">Register Customer</h2>
    <div class="center-position">
      <form class="formStyle">
        <fieldset>
          <div>
            <label class="formLabel">Username:</label><br>
            <input type="text" v-model="userRegistration.username" name="username" class="formInput">
          </div>
          <div>
            <label class="formLabel">Password:</label><br>
            <input type="password" v-model="userRegistration.password" name="password" class="formInput">
          </div>
          <div>
            <label class="formLabel">Confirm Password:</label><br>
            <input type="password" v-model="confirmPassword" name="confirmPassword" class="formInput">
          </div>
          <div>
            <label class="formLabel">First Name:</label><br>
            <input type="text" v-model="userRegistration.name" name="firstName" class="formInput">
          </div>
          <div>
            <label class="formLabel">Last Name:</label><br>
            <input type="text" v-model="userRegistration.surname" name="lastName" class="formInput">
          </div>
          <div>
            <label class="formLabel">Gender:</label><br>
            <select v-model="userRegistration.gender" name="gender" class="formInput">
              <option value="">Select</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
            </select>
          </div>
          <div>
            <label class="formLabel">Date of Birth:</label><br>
            <input type="date" v-model="userRegistration.birthDate" name="dateOfBirth" class="formInput">
          </div>
          <div>
            <button type="submit" @click="registerCustomer" class="formButton">Register</button>
          </div>
          <div>
            <button @click="goToLoginPage" class="formButton">Go to Login</button>
          </div>
        </fieldset>
      </form>
      <p style="color: #FF6347;">{{ errortext }}</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import router from '@/router';

export default {
  data() {
    return {
      userRegistration: { username: null, password: null, name: null, surname: null, gender: null, birthDate: null },
      confirmPassword: '',
      errortext: '',
      user: { username: null, password: null, name: null, surname: null, gender: null, birthDate: null },
    };
  },
  methods: {
    registerCustomer(event) {
      event.preventDefault();

      // Reset previous error styling
      document.querySelectorAll(".formInput").forEach(input => {
        input.style.background = "white";
      });

      // Validation checks
      if (!this.userRegistration.username || !this.userRegistration.password || !this.confirmPassword ||
          !this.userRegistration.name || !this.userRegistration.surname || !this.userRegistration.gender ||
          !this.userRegistration.birthDate) {
        document.querySelectorAll(".formInput").forEach(input => {
          if (!input.value) {
            input.style.background = "red";
          }
        });
        this.errortext = 'All fields are required';
        return;
      }

      if (this.userRegistration.password !== this.confirmPassword) {
        document.getElementsByName("confirmPassword")[0].style.background = "red";
        this.errortext = 'Passwords do not match';
        return;
      }

      const today = new Date();
      const selectedDate = new Date(this.userRegistration.birthDate);
      if (selectedDate >= today) {
        document.getElementsByName("dateOfBirth")[0].style.background = "red";
        this.errortext = 'Birth date must be in the past';
        return;
      }

      // If all validations pass, proceed with registration
      axios.post('http://localhost:8080/WebShopAppREST/rest/user/register/c/', this.userRegistration)
        .then(response => {
          this.user = response.data;
          console.log(`User id: ${this.user.id}`);
          if (!this.user.id) {
            this.errortext = 'This username is already in use. Try another one.';
            document.getElementsByName("username")[0].style.background = "red";
          } else {
            router.push(`/login`);
          }
        })
        .catch(error => {
          console.error('An error occurred:', error);
          this.errortext = 'An error occurred during registration';
        });
    },
    goToLoginPage(event) {
      event.preventDefault();
      router.push(`/login`);
    }
  }
};
</script>

<style scoped>
.center-position {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.formStyle {
  width: 300px;
  background-color: #333;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.formLabel {
  color: #FFF;
  font-weight: bold;
  margin-bottom: 5px;
}

.formInput {
  width: calc(100% - 12px);
  padding: 6px;
  margin-bottom: 10px;
  border: 1px solid #555;
  border-radius: 4px;
  background-color: #444;
  color: #FFF;
}

.formButton {
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  border: none;
  border-radius: 4px;
  background-color: #FF6347;
  color: #FFF;
  cursor: pointer;
}

.formButton:hover {
  background-color: #FF4500;
}

p {
  color: #FF6347;
  text-align: center;
  margin-top: 10px;
}

legend {
  font-weight: bold;
}
</style>
