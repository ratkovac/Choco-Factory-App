<template>
  <div class="center-position">
    <form class="formStyle" style="margin-top:5%;" @submit.prevent="loginUser">
      <fieldset>
        <legend style="color: #FFF;">Log in</legend>
        <div>
          <label for="username" class="formLabel">Username</label><br>
          <input type="text" v-model="userCredentials.username" name="username" id="username" class="formInput">
        </div>
        <div>
          <label for="password" class="formLabel">Password</label><br>
          <input type="password" v-model="userCredentials.password" name="password" id="password" class="formInput">
        </div><br>
        <div>
          <button type="submit" class="formButton">Login</button>
        </div>
        <p style="color: red;">{{ errortext }}</p>
      </fieldset><br>
      <div>
        <button type="button" @click="registerUser" class="formButton">Register</button>
      </div><br>
      <div>
        <button type="button" @click="goBack" class="formButton">Return</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import router from '@/router';

export default {
  data() {
    return {
      user: null,
      factoryId: null,
      userCredentials: {
        username: null,
        password: null
      },
      isUsernameValid: null,
      isPasswordValid: null,
      errortext: ''
    };
  },
  methods: {
    loginUser() {
      if (!this.userCredentials.username) {
        document.getElementsByName("username")[0].style.background = "red";
        this.isUsernameValid = false;
        this.errortext = 'All fields are required';
      } else {
        document.getElementsByName("username")[0].style.background = "white";
        this.isUsernameValid = true;
        this.errortext = '';
      }
      if (!this.userCredentials.password) {
        document.getElementsByName("password")[0].style.background = "red";
        this.isPasswordValid = false;
        this.errortext = 'All fields are required';
      } else {
        document.getElementsByName("password")[0].style.background = "white";
        this.isPasswordValid = true;
        this.errortext = '';
      }
      if (this.isUsernameValid && this.isPasswordValid) {
        axios.post('http://localhost:8080/WebShopAppREST/rest/user/login/', this.userCredentials)
          .then(response => {
            if (response.data) {
              const responseData = response.data;
              this.user = responseData.user || responseData;
              this.factoryId = responseData.factoryId || (this.user.factory ? this.user.factory.id : null);
              const userId = responseData.id;
              if (this.user.status === 'DEACTIVATED') {
                this.errortext = 'Your account has been deactivated.';
                return;
              }
              if (this.user.role === 'Administrator') {
                router.push({ path: `/factories/admin/${this.user.id}` });
                router.push({ 
                  path: `/adminHome/${this.factoryId}`, 
                  query: {
                    id: userId,
                    firstName: responseData.firstName,
                    lastName: responseData.lastName,
                    username: responseData.username
                  }
                });
              } else if (this.user.role === 'Manager') {
                if (this.factoryId) {
                  router.push({ 
                    path: `/factories/manager/${this.factoryId}`, 
                    query: {
                      id: userId,
                      firstName: responseData.firstName,
                      lastName: responseData.lastName,
                      username: responseData.username
                    }
                  });
                } else {
                  this.errortext = 'Factory ID not found';
                }
              } else if (this.user.role === 'Worker') {
                router.push({ 
                  path: `/factories/worker/${this.factoryId}`, 
                  query: {
                    id: userId,
                    firstName: responseData.firstName,
                    lastName: responseData.lastName,
                    username: responseData.username
                  }
                });
              } else if (this.user.role === 'Customer') {
                router.push({ 
                  path: `/factories/customer/${userId}`, 
                  query: {
                    id: userId,
                    firstName: responseData.firstName,
                    lastName: responseData.lastName,
                    username: responseData.username
                  }
                });
              } else {
                this.errortext = 'Invalid user role';
              }
            } else {
              this.errortext = `User does not exist with username: "${this.userCredentials.username}"`;
            }
          })
          .catch(error => {
            console.error('An error occurred:', error);
            this.errortext = `User does not exist with username: "${this.userCredentials.username}"`;
          });
      }
    },
    registerUser() {
      router.push(`/register`);
    },
    goBack() {
      router.push(`/`);
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
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
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

.formInput:focus {
  outline: none;
  border-color: #573b8a;
  color: #FFF; /* Dodajte boju teksta za fokusirano stanje */
}

.formButton {
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  background-color: #573b8a;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.formButton:hover {
  background-color: #6d44b8;
}

p {
  color: red;
  text-align: center;
  margin-top: 10px;
}
</style>
