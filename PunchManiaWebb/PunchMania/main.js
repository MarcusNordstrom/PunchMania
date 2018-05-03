if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('sw.js')
      .then(function(reg){
      }).catch(function(err) {
        console.log("SW error: ", err)
      });
  }