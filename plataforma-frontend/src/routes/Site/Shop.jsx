import React, {useState} from 'react'
import ShopCards from '../../components/site/ShopCards/ShopCards'
import MenuNavbar from "../../components/site/MenuNavbar/MenuNavbar.jsx";
import BreadCrumbs from "../../components/site/BreadCrumbs/BreadCrumbs.jsx";
import Footer from "../../components/site/Footer/Footer.jsx";

const barraCrumbs = [
  {
    type: "a",
    name: "Home", 
    ref: "/"
  },
  {
    type: "a",
    name: "Shop", 
    ref: "/shop"
  },
];



const Shop = () => {

    const [cartItems, setCartItems] = useState([]);

    const handleAddToCart = (productData) => {
        setCartItems([...cartItems, productData]);
    };

  return (
      <div className="h-screen bg-gray-900 w-full overflow-y-auto">
          <MenuNavbar />

          <div className="p-2 w-full">
              <BreadCrumbs itensCrumbs={barraCrumbs}/>
          </div>

          <div>
              <ShopCards onAddToCart={handleAddToCart}/>
          </div>

        <Footer/>


      </div>

  );
};


export default Shop;
