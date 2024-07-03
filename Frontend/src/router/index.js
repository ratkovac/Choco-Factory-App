import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/RegisterView.vue'
import Coco from '@/views/CocoView.vue'
import CocoUpdate from '@/views/CocoUpdateView.vue'
import FactoryWorker from '@/views/FactoryWorkerView.vue'
import Factory from '@/views/FactoryView.vue'
import FactoryAdmin from '@/views/FactoryAdminView.vue'
import FactoryManager from '@/views/FactoryManagerView.vue'
import FactoryCustomer from '@/views/FactoryCustomerView.vue'
import FactoryReview from '@/views/FactoryReviewView.vue'
import CreateFactory from '@/views/CreateFactoryView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    },
    {
      path: '/chocolates/:cocoId/:name', // Dodan ":" prije cocoId
      name: 'cocoUpdate',
      component: Coco
    },
    {
      path: '/chocolates/update/:cocoId/:name', // Dodan ":" prije cocoId
      name: 'cocoUpdateWorker',
      component: CocoUpdate
    },
    {
      path: '/chocolates',
      name: 'chocolates', // Ispravljeno ime rute
      component: Coco
    },
    {
      path: '/chocolates/factories/:factoryId/:name', // ":" za factoryId i ":name"
      name: 'cocoCreate',
      component: Coco
    },    
    {
      path: '/factories/:id', // Dodajte ":id" da biste očekivali parametar ID
      name: 'factoryReview',
      component: FactoryReview,
      props: true // Proslijedite parametre kao props
    },
    {
      path: '/chocolates/add', // Dodan ":" prije factoryId
      name: 'cocoSave',
      component: Login
    },
    {
      path: '/factories',
      name: 'factory',
      component: Factory
    },
    {
      path: '/factories/admin',
      name: 'factoryAdmin',
      component: FactoryAdmin
    },
    {
      path: '/factories/customer/:id',
      name: 'factoryCustomer',
      component: FactoryCustomer
    },
    {
      path: '/factories/worker/:id',
      name: 'factoryWorker',
      props: true,
      component: FactoryWorker
    },
    {
      path: '/factories/admin/create',
      name: 'createFactory',
      component: CreateFactory
    },
    {
      path: '/factories/manager/:id',
      name: 'factoryManager',
      props: true,
      component: FactoryManager
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    }
  ]
})

export default router
