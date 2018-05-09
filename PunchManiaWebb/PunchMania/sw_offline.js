self.addEventListener('install', function(event) {
  var offlinePage = new Request('offline.html');
  event.waitUntil(
    fetch(offlinePage).then(function(response) {
      return caches.open('offline').then(function(cache) {
        console.log('Cached offline page during Install ' + response.url);
        return cache.put(offlinePage, response);
      });
    }));
});

self.addEventListener('fetch', function(event) {
  if (event.request.cache === 'only-if-cached' && event.request.mode !== 'same-origin') {
    return;
  }
  event.respondWith(
    fetch(event.request).catch(function(error) {
      console.error('Network request Failed. Serving offline page ' + error);
      return caches.open('offline').then(function(cache) {
        return cache.match('offline.html');
      });
    }));
});

self.addEventListener('refreshOffline', function(response) {
  return caches.open('offline').then(function(cache) {
    console.log('Offline page updated from refreshOffline event: ' + response.url);
    return cache.put(offlinePage, response);
  });
});
