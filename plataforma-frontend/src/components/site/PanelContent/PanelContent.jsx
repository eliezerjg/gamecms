import React, {useState} from 'react';
import './styles.css';
import Security from './Tabs/Security/Security.jsx';
import Guildmark from './Tabs/Guildmark/Guildmark.jsx';
import Purchases from "./Tabs/Purchases/Purchases.jsx";
import i18n from "../../../lib/i18n.jsx";

const itemsMenu = [
    { name: i18n.t('site.components.PanelContent.itemsMenu.password'), component: <Security /> },
    { name: i18n.t('site.components.PanelContent.itemsMenu.guildmark'), component: <Guildmark /> },
    { name: i18n.t('site.components.PanelContent.itemsMenu.purchases'), component: <Purchases /> },
];

function PanelContent() {
  const [selectedComponent, setSelectedComponent] = useState(itemsMenu[0].componente);

  const handleViewContentChange = (component) => {
    setSelectedComponent(component);
  };

  return (
    <div className="flex flex-grow">
      <div className="w-1/6 bg-gray-800 p-4 menu-painel">
        <ul>
          <strong className='text-white w-1 text-2xl'>{i18n.t('site.components.PanelContent.content')}</strong>
          <hr />

          {itemsMenu.map((item) => (
            <li key={item.name}>
                <button
                    className={`btn bg-red-800  mt-2 w-full ${selectedComponent === item.component ? 'text-sm text-white border border-white' : 'text-white'}`}
                    onClick={() => handleViewContentChange(item.component)}>
                    {item.name}
                </button>
            </li>
          ))}
        </ul>
      </div>
      <div className="flex-grow p-4 conteudo-painel bg-gray-900 ">
        {selectedComponent}
      </div>
    </div>
  );
}

export default PanelContent;
