const customerId = "1";
const recaptchakey = "6LcciB0pAAAAAPoOurQcrJYb_1mLxaHdE69DToC-";
const baseCurrency = {
    "sign" : "R$",
    "name" : "BRL"
}

const profiles = {
    production : {
        backendCentralizado : `https://back.gamecms.com.br/api/v1/customer/${customerId}`,
        integracaoUrl: `https://i-customer-${customerId}.gamecms.com.br/api/v1`,
        recaptchakey : recaptchakey,
        currency : baseCurrency
    },
    development : {
        backendCentralizado : `http://localhost:8777/api/v1/customer/${customerId}`,
        integracaoUrl: `https://i-customer-${customerId}.gamecms.com.br/api/v1`,
        recaptchakey : recaptchakey,
        currency : baseCurrency
    }
};

class Configuration{
    profile = profiles.production;
    baseUrl = this.profile.backendCentralizado;
    integracaoUrl = this.profile.integracaoUrl;
    recaptchakey = this.profile.recaptchakey;
    profiles = profiles;
    currency = this.profile.currency;

    getBaseUrl = () => this.baseUrl;

    getRecaptchaPublicKey = () => this.recaptchakey;

    getIntegracaoUrl = () => this.integracaoUrl;

    getProfiles = () => this.profiles;

    getCurrency = () => this.currency;
}

const configurationSingleton = new Configuration();



export default configurationSingleton;

