(function (gaScript, script) {
    window.GoogleAnalyticsObject = 'ga';
    window.ga = window.ga || function () {
            (window.ga.q = window.ga.q || []).push(arguments);
        };
    window.ga.l = 1 * new Date();
    gaScript = document.createElement('script');
    script = document.getElementsByTagName('script')[0];
    gaScript.async = 1;
    gaScript.src = '//www.google-analytics.com/analytics.js';
    script.parentNode.insertBefore(gaScript, script);
})();
ga('create', 'UA-64472741-1', 'auto');
ga('require', 'displayfeatures');
ga('send', 'pageview');