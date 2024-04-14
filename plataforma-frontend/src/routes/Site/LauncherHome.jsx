import React from 'react'
import MenuNavbar from "../../components/site/MenuNavbar/MenuNavbar.jsx";
import Footer from "../../components/site/Footer/Footer.jsx";


const LauncherHome = () => {
  return (
    <div>
      <MenuNavbar/>
        <div>
          <h1 className="text-center font-mono text-lg text-white"> Bem vindos WYDianos </h1>
          <p className="text-base mx-auto  text-center text-white text-lg">
          Na origem do mundo de WYD, Yetzirah e Tzfah eram os supremos, gerando vida e deuses. Cinco filhos divinos emergiram, mas Tzfah os prendeu, causando sofrimento humano. Com auxílio de Yetzirah, os deuses se libertaram, desencadeando uma batalha. Prenderam Tzfah, mas ele influencia a maldade. Anos depois, mago Nershess cede à escuridão, provocando conflitos. Humanos buscam magia para se reerguer em reinos. Quatro tipos de personagens - Huntress, Beastmaster, Foema e Transknight - moldam o destino em meio a desafios e poderes, marcando o mundo de WYD.
          </p>
        </div>
      <Footer/>
    </div>
  )
}

export default LauncherHome