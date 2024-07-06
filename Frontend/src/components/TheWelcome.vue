<script setup>
</script>

<template>
  <section class="welcome">
    <div class="links">
      <RouterLink to="/login">Login</RouterLink>
      <RouterLink to="/register">Sign up</RouterLink>
    </div>
  </section>
</template>

<style scoped>
.welcome {
  text-align: center;
  margin-top: 0px;
  font-family: Arial, sans-serif;
}

.links {
  text-align: center;
}

.links a {
  display: inline-block;
  color: white;
  background-color: black;
  text-decoration: none;
  font-size: 18px;
  padding: 10px 20px;
  margin-right: 10px; /* Dodajemo razmak između linkova */
  border-radius: 5px;
  transition: background-color 0.3s ease;
  width: 300px;
}

.links a:hover {
  background-color: #333;
}
</style>
