<template>
    <section class="py-5">
      <div class="container px-4 px-lg-5 my-5">
        <div class="row gx-5 gx-lg-1 align-items-center">
          <div class="col-md-120">
            <div class="small mb-1" :style="{ margin: '20px 30px 0px 140px' }">{{ factoryName }}</div>
            <h1 class="display-7 fw-bolder" :style="{ margin: '0px 0px 0px 95px' }">{{ title }}</h1>
            <div class="card h-100">
              <form class="card-body custom-form" @submit.prevent="saveChocolate()">
                <div class="form-group">
                  <label for="name">Name:</label><br>
                  <input type="text" id="name" v-model="coco.name" :class="{ 'error': !validation.nameValid }" class="form-control custom-input">
                </div>
                <div class="form-group">
                  <label for="type">Type:</label><br>
                  <select id="type" v-model="coco.type" :class="{ 'error': !validation.typeValid }" class="form-control custom-input">
                    <option value="Milk">Milk</option>
                    <option value="Dark">Dark</option>
                    <option value="White">White</option>
                    <option value="Hazelnut">Hazelnut</option>
                  </select>
                </div>
                <div class="form-group">
                  <label for="category">Category:</label><br>
                  <select id="category" v-model="coco.category" :class="{ 'error': !validation.categoryValid }" class="form-control custom-input">
                    <option value="Bar">Bar</option>
                    <option value="Truffle">Truffle</option>
                    <option value="Biscuit">Biscuit</option>
                    <option value="Praline">Praline</option>
                  </select>
                </div>
                <div class="form-group">
                  <label for="weight">Weight:</label><br>
                  <input type="number" id="weight" v-model="coco.mass" :class="{ 'error': !validation.massValid }" class="form-control custom-input">
                </div>
                <div class="form-group">
                  <label for="price">Price:</label><br>
                  <input type="number" id="price" v-model="coco.cost" :class="{ 'error': !validation.costValid }" class="form-control custom-input">
                </div>
                <div class="form-group">
                  <label for="description">Description:</label><br>
                  <textarea id="description" v-model="coco.details" :class="{ 'error': !validation.descriptionValid }" class="form-control custom-input-textarea"></textarea>
                </div>
                <div class="form-group">
                  <label for="image">Image Url:</label><br>
                  <input type="text" id="image" v-model="coco.picture" :class="{ 'error': !validation.imageValid }" class="form-control custom-input">
                </div>
                <div class="form-group">
                  <img class="card-img-top mb-5 mb-md-0" :src="coco.picture" alt="url">
                </div>
                <div class="form-group">
                    <button class="btn btn-outline-dark flex-shrink-0 custom-button" type="submit">Save Chocolate</button>
                </div>                
              </form>
            </div>
          </div>
        </div>
      </div>
    </section>
</template>
  
  
  
  <script setup>
  import axios from 'axios';
  import { onMounted, ref } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  
  const validation = ref({ nameValid: true, costValid: true, categoryValid: true, typeValid: true, massValid: true, descriptionValid: true, imageValid: true });
  const route = useRoute();
  const router = useRouter();
  const factoryId = ref(route.params.factoryId);
  const factoryName = route.params.name;
  const coco = ref({
    id: "",
    name: "",
    cost: 0.0,
    category: "",
    type: "",
    mass: 0.0,
    details: "",  
    picture: "", 
    stock: 0,
    isDeleted: false,
    factoryId: "",
    status: "nema"
});

  const errorMessages = ref({ nameError: "", typeError: "", priceError: "", categoryError: "", weightError: "", descriptionError: "", imageError: "" });
  const id = ref(route.params.cocoId);
  const title = ref("Add Chocolate");
  
  onMounted(() => {
    if (id.value) {
      title.value = "Update Chocolate";
      load();
    }
  });
  
  function load() {
    axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/${id.value}`)
        .then(response => {
            if (!response.data) {
                errorMessages.value.errorMessage = "Chocolate cannot be find!";
            } else {
                coco.value = response.data;
                console.log(coco);
            }
        })
        .catch(error => {
            console.error(error);
            errorMessages.value.errorMessage = "Chocolate wasn't able to be saved! Error";
        });
  }

  function saveChocolate() {
    validateField('name');
    validateField('type');
    validateField('cost');
    validateField('category');
    validateField('mass');
    validateField('details');
    validateField('picture');

    if (isFormValid()) {
      if (id.value) {
        updateChocolate();
      } else {
        addChocolate();
      }
    }
  }

  function validateField(field) {
    validation.value[`${field}Valid`] = !!coco.value[field];

    if (!coco.value[field]) {
      errorMessages.value[`${field}Error`] = `Please enter a ${field}`;
    } else {
      errorMessages.value[`${field}Error`] = '';
    }
    console.log(`Validating field: ${field}`);

  }
  

  function isFormValid() {
    console.log("Uslo");
    for (const field in validation.value) {
      if (!validation.value[field]) {
        return false;
      }
    }
    console.log('Form is valid!');
    return true;
  }

  function updateChocolate() {
    console.log('updating chocolate...');
    console.log(coco.value.name);
    axios.put(`http://localhost:8080/WebShopAppREST/rest/chocolates/${id.value}`, coco.value)
      .then(response => {
        if (!response.data) {
          errorMessages.value.errorMessage = "Chocolate wasn't able to be saved! Error";
        } else {
          router.back();
        }
      })
      .catch(error => {
        console.error(error);
        errorMessages.value.errorMessage = "Chocolate wasn't able to be saved! Error";
      });
  }

  function addChocolate() {
    console.log('Adding chocolate...');
    coco.value.factoryId = factoryId.value;
    console.log(coco.value.name);
    axios.post('http://localhost:8080/WebShopAppREST/rest/chocolates/add', coco.value)
      .then(response => {
        if (!response.data) {
          errorMessages.value.errorMessage = "Chocolate wasn't able to be saved! Error";
        } else {
          router.back();
        }
      })
      .catch(error => {
        console.error(error);
        errorMessages.value.errorMessage = "Chocolate wasn't able to be saved! Error";
      });
  }
</script>

<style scoped>
.custom-form {
  border: 1px solid #ddd; 
  padding: 20px;
  border-radius: 5%;
  background-color: darkslategray; 
}
.error-message {
  color: red;
}

img {
  max-width: 80x;
  max-height: 50px;
}

.error {
  border: 2px solid rgb(241, 53, 19);
}

.custom-input {
  width: 300px;
  height: 25px;
  font-size: 14px;
  font-family: Arial, sans-serif;
  font-weight: 545;
  margin-bottom: 10px;
}

.custom-input-textarea {
  width: 300px;
  height: 80px; 
  font-size: 14px;
  font-family: Arial, sans-serif;
  font-weight: 545;
  margin-bottom: 10px;
}
.custom-input,
button.custom-button {
  width: 300px; 
  height: 25px; 
  font-size: 14px;
  font-family: Arial, sans-serif;
  font-weight: 545;
  margin-bottom: 10px;
}

</style>
  