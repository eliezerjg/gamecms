import "./styles.css";
import i18n from '../../../lib/i18n.jsx';
import LanguageSwitcher from "../LanguageSwitcher/LanguageSwitcher.jsx";
import React from "react";

function BrandingLine() {


  return (
      <div>
          <div className="line mx-auto text-white text-lg p-1 w-100 py-2" onClick={() => {window.location.href = "https://gamecms.com.br"}}>
              <p className="text-left text-sm ml-2"> {i18n.t('site.components.BrandingLine.content')} </p>
          </div>
      </div>



  );
}

export default BrandingLine;
