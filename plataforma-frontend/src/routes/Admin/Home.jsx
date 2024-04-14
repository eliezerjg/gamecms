import React from 'react'

import MenuNavbar from '../../components/admin/MenuNavbar/MenuNavbar.jsx'
import Footer from "../../components/site/Footer/Footer.jsx";
import i18n from "../../lib/i18n.jsx"

const Home = () => {

  return (
      <div className='h-screen bg-base-200'>
          <MenuNavbar/>
          <div className="container mx-auto mt-12">
              <div className="w-full p-4">
                  <h1 className='text-center text-black text-3xl'><strong>
                      {i18n.t('admin.routes.Home.content')}
                  </strong></h1>
                  <br/>
                  <p className="text-center text-black text-2xl mt-10">
                      {i18n.t('admin.routes.Home.text')}
                  </p>
              </div>

          </div>

          <Footer/>
      </div>
  )
}

export default Home