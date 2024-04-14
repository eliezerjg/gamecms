import React, {useState} from 'react';
import axios from 'axios';
import "./styles.css";
import Swal from 'sweetalert2';
import ReCAPTCHA from "react-google-recaptcha";
import configurationSingleton from "../../../../../middlewares/Configurations.jsx";
import Configurations from "../../../../../middlewares/Configurations.jsx";
import i18n from "../../../../../lib/i18n.jsx"

let recaptcha;
const updateRecatpcha = (capturedCaptcha) => {
  recaptcha = capturedCaptcha;
};


function Security() {
  const [formData, setFormData] = useState({
  });

  

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  let recaptcha;
  const updateRecatpcha = (capturedCaptcha) => {
    recaptcha = capturedCaptcha;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    formData.user = localStorage.getItem('username');


    if (/[^a-zA-Z0-9]/.test(formData.password)) {
      Swal.fire({
        icon: 'error',
        title: i18n.t('site.components.PanelContent.tabs.security.handleSubmit.error'),
        text: i18n.t('site.components.PanelContent.tabs.security.handleSubmit.shouldHaveOnlyLettersAndNumbers'),
      });
      return;
    }

    try {
      formData.recaptcha = recaptcha;
      const token = localStorage.getItem('jwt');
      
      const response = await axios.patch(`${configurationSingleton.getIntegracaoUrl()}/account/changePass`, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      Swal.fire(
          i18n.t('site.components.PanelContent.tabs.security.handleSubmit.success'),
          i18n.t('site.components.PanelContent.tabs.security.handleSubmit.successText'),
        'success'
      )

    } catch (error) {
      Swal.fire(
          i18n.t('site.components.PanelContent.tabs.security.handleSubmit.error'),
        error.response.data.message + ' Status: ' + error.response.status + '.',
        'error'
      );
    }
  };

  return (
    <div className="password-change-form">
      <h1 className='text-2xl text-center font-semibold mb-4 text-white'>{i18n.t('site.components.PanelContent.tabs.security.content')}</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="password">{i18n.t('site.components.PanelContent.tabs.security.actualPassword')}</label>
          <input
            type="password"
            id="password"
            name="password"
            placeholder={i18n.t('site.components.PanelContent.tabs.security.actualPasswordPlaceHolder')}
            className="w-full px-4 py-2 border rounded focus:ring focus:ring-blue-300"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="newPassword">{i18n.t('site.components.PanelContent.tabs.security.newPassword')}</label>
          <input
            type="password"
            id="newPassword"
            name="newPassword"
            placeholder={i18n.t('site.components.PanelContent.tabs.security.newPasswordPlaceHolder')}
            className="w-full px-4 py-2 border rounded focus:ring focus:ring-blue-300"
            value={formData.newPassword}
            onChange={handleChange}
            required
          />
        </div>

        <ReCAPTCHA
            sitekey={Configurations.getRecaptchaPublicKey()}
            size="normal"
            onChange={updateRecatpcha}
            className="mb-4 mt-4"
        />

        <div className="form-group py-4 w-full">
       
        <button type="submit" className="btn btn-primary w-full">{i18n.t('site.components.PanelContent.tabs.security.changePassword')}</button>
        </div>
      </form>
    </div>
  );
}

export default Security;
