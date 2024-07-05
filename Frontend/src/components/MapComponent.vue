<script>
import 'ol/ol.css';
import { Map, View } from 'ol';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import { fromLonLat, toLonLat } from 'ol/proj';
import { defaults as defaultControls } from 'ol/control';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Point } from 'ol/geom';
import { Feature } from 'ol';
import { Style, Icon } from 'ol/style';

export default {
  name: 'MapComponent',
  props: {
    location: {
      type: Object,
      required: true,
      default: () => ({ lat: 0, lon: 0 })
    }
  },
  data() {
    return {
      map: null,
      vectorLayer: null,
      searchQuery: ''
    };
  },
  methods: {
    async searchLocation() {
      const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${this.searchQuery}`);
      const data = await response.json();
      if (data.length > 0) {
        const { lat, lon } = data[0];
        this.setMarker(lon, lat);
        this.$emit('update-location', { lat, lon }); // Emitujemo ažuriranje lokacije
        this.map.getView().setCenter(fromLonLat([parseFloat(lon), parseFloat(lat)]));
        this.map.getView().setZoom(10);
      } else {
        alert('Location not found');
      }
    },
    setMarker(lon, lat) {
      const coordinates = fromLonLat([lon, lat]);
      this.vectorLayer.getSource().clear();
      const marker = new Feature({
        geometry: new Point(coordinates)
      });
      marker.setStyle(new Style({
        image: new Icon({
          src: 'https://openlayers.org/en/latest/examples/data/icon.png',
          scale: 0.1
        })
      }));
      this.vectorLayer.getSource().addFeature(marker);
    },
    handleMapClick(event) {
      const coordinates = this.map.getEventCoordinate(event);
      const [lon, lat] = toLonLat(coordinates);
      this.$emit('update-location', { lat, lon });
      this.setMarker(lon, lat);
    }
  },
  mounted() {
    this.vectorLayer = new VectorLayer({
      source: new VectorSource()
    });

    this.map = new Map({
      target: 'map',
      layers: [
        new TileLayer({
          source: new OSM()
        }),
        this.vectorLayer
      ],
      view: new View({
        center: fromLonLat([this.location.lon, this.location.lat]), // Početna lokacija centra mape
        zoom: 4
      }),
      controls: defaultControls()
    });

    this.map.on('click', this.handleMapClick);

    this.setMarker(this.location.lon, this.location.lat); // Postavi marker na početnu lokaciju
  }
};
</script>

<style scoped>
#map {
  width: 100%;
  height: 300px;
}
.search-bar {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}
.search-bar input {
  width: 300px;
  padding: 5px;
  font-size: 16px;
}
.search-bar button {
  padding: 5px 10px;
  font-size: 16px;
  margin-left: 5px;
  cursor: pointer;
}
</style>
