import React from 'react';
import styles from './styles.module.css';
import { Link } from 'react-router-dom';



function BreadCrumbs({itensCrumbs}) {
  return (
    <div className="text-lg breadcrumbs bg-base-100 p-2 w-100 rounded border border-none" data-theme="dark">
      <ul className="flex">
        {itensCrumbs.map((item, index) => (
          <li key={index} className="flex items-center">
            {item.type === "a" ? (
              <Link to={item.ref} className={`${styles.link} text-white`}>{item.name}</Link>
            ) : (
              <span className="text-white">{item.name}</span>
            )}
            {index !== itensCrumbs.length - 1 && <span className="mx-1 text-white">  </span>}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default BreadCrumbs;
