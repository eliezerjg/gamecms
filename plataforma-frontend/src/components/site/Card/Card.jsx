import React from 'react';
import { Link } from "react-router-dom";
import i18n from "../../../lib/i18n.jsx";


function Card({ item }) {
  const formatDate = (date) => {
    const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
    return new Date(date).toLocaleDateString('pt-BR', options);
  };

  return (
    <div className="rounded shadow">
      <div className="card w-100 bg-base-100 shadow-xl">
        <div className="card-body">
          <h2 className="card-title text-xl font-semibold">{item.title}</h2>


            <p className="text-gray-500" dangerouslySetInnerHTML={{ __html: `${item.title} - ${item.description.length > 20 ? `${item.description.substring(0, 20)}...` : item.description} - ${i18n.t('site.components.Card.authoredBy')}: ${item.author} ${i18n.t('site.components.Card.at')} ${formatDate(item.data)}` }}></p>


            <div className="flex justify-end mt-4">

            <Link to={"/post/" + item.id}>
              <button className="btn btn-primary py-2 px-6">{ i18n.t('site.components.Card.readMore') }</button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Card;
