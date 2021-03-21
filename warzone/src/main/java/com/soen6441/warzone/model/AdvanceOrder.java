package com.soen6441.warzone.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Map;


/**
 * This Class is used for The advance order Command Three annotations
 * (Getter,Setter, toString), you can see on the top of the class are lombok
 * dependencies to automatically generate getter, setter and tostring method in
 * the code.
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@Getter
@Setter
@ToString
public class AdvanceOrder extends Order{

    /**
     * country in this order
     */
    private String d_CountryNameFrom;


    /**
     * country on which armies have to deployed
     */
    private String d_CountryNameTo;

    /**
     * number of armies in this order
     */
    private int d_noOfArmies;

    /**
     * No of mandatory fields It always needs to have after all necessary fields
     */
    public int d_mandatoryField = 3;

    @Override
    public boolean executeOrder() {
        Country l_countryfrom = getPlayerCountrybyName(d_CountryNameFrom);
        Country l_countryTo = getPlayerCountrybyName(d_CountryNameTo);
        boolean l_flag=true;
        Player l_targetPlayer = null;
        if (d_player.getD_ownedCountries().contains(l_countryfrom)) {
            int l_countryFromIndex=d_player.getD_ownedCountries().indexOf(l_countryfrom);
            int l_playerFromIndex=d_gameData.getD_playerList().indexOf(d_player);
            int l_countryToIndex=-1;
            int l_playerToIndex=-1;

            int l_fromArmies = l_countryfrom.getD_noOfArmies();
            if(l_fromArmies<d_noOfArmies)
            {
                return false;
            }
            if (d_player.getD_ownedCountries().contains(l_countryTo) && isneighbourCountry(d_CountryNameTo,l_countryfrom)) {

                int l_toArmies = l_countryTo.getD_noOfArmies();
                l_countryToIndex=d_player.getD_ownedCountries().indexOf(l_countryTo);
                if (l_countryfrom.getD_noOfArmies() >= d_noOfArmies) {
                    l_fromArmies=l_fromArmies-d_noOfArmies;
                    l_toArmies=l_toArmies+d_noOfArmies;
                    l_countryfrom.setD_noOfArmies(l_fromArmies);
                    l_countryTo.setD_noOfArmies(l_toArmies);
                } else {
                    l_fromArmies=0;
                    l_toArmies=l_toArmies+l_fromArmies;
                    l_countryfrom.setD_noOfArmies(l_fromArmies);
                    l_countryTo.setD_noOfArmies(l_toArmies);
                }

                d_player.getD_ownedCountries().remove(l_countryToIndex);
                d_player.getD_ownedCountries().add(l_countryFromIndex,l_countryfrom);
                d_player.getD_ownedCountries().remove(l_countryFromIndex);
                d_player.getD_ownedCountries().add(l_countryToIndex,l_countryTo);
                d_gameData.getD_playerList().remove(l_playerFromIndex);
                d_gameData.getD_playerList().add(l_playerFromIndex,d_player);

                if (d_gameData.getD_warMap().getD_continents() != null) {
                    for (Map.Entry<Integer, Continent> l_entry : d_gameData.getD_warMap().getD_continents().entrySet()) {
                        for (Country l_countries : l_entry.getValue().getD_countryList()) {
                            if (l_countries.getD_countryName().equalsIgnoreCase(d_CountryNameFrom)) {
                                l_countries.setD_noOfArmies(l_fromArmies);                              //sets the no. of armies to the country of map
                            }
                            if (l_countries.getD_countryName().equalsIgnoreCase(d_CountryNameTo)) {
                                l_countries.setD_noOfArmies(l_toArmies);                              //sets the no. of armies to the country of map
                            }
                        }
                    }
                }
                return true;
            }

           /* else if (!isneighbourCountry(d_CountryNameTo,l_countryfrom) && l_countryTo!=null ) {
                System.out.println("to is null");
                if (l_fromArmies >= d_noOfArmies) {
                    l_fromArmies=l_fromArmies-d_noOfArmies;
                    l_countryfrom.setD_noOfArmies(l_fromArmies);
                } else {
                    l_fromArmies=0;
                    l_countryfrom.setD_noOfArmies(l_fromArmies);
                }
                d_player.getD_ownedCountries().remove(l_countryFromIndex);
                d_player.getD_ownedCountries().add(l_countryFromIndex,l_countryfrom);
                d_gameData.getD_playerList().remove(l_playerFromIndex);
                d_gameData.getD_playerList().add(l_playerFromIndex,d_player);

                if (d_gameData.getD_warMap().getD_continents() != null) {
                    for (Map.Entry<Integer, Continent> l_entry : d_gameData.getD_warMap().getD_continents().entrySet()) {
                        for (Country l_countries : l_entry.getValue().getD_countryList()) {
                            if (l_countries.getD_countryName().equalsIgnoreCase(d_CountryNameFrom)) {
                                l_countries.setD_noOfArmies(l_fromArmies);                              //sets the no. of armies to the country of map
                            }
                        }
                    }
                }

                return false;
            }*/

            else {
                if (isneighbourCountry(d_CountryNameTo,l_countryfrom))
                {
                    for (Player l_player : d_gameData.getD_playerList())
                    {
                        if (l_player.getD_ownedCountries().contains(l_countryTo))
                        {
                            l_targetPlayer = l_player;
                            l_countryToIndex=l_player.getD_ownedCountries().indexOf(l_countryTo);
                            l_playerToIndex=d_gameData.getD_playerList().indexOf(l_player);

                        }
                    }

                    int l_toArmies = l_countryTo.getD_noOfArmies();
                    int l_attackArmiesFrom=(int) Math.round(d_noOfArmies*0.6);
                    int l_attackArmiesTo=(int) Math.round(l_toArmies*0.7);
                    if(l_attackArmiesFrom>=l_toArmies)
                    {
                        l_toArmies=d_noOfArmies-l_attackArmiesTo;
                        l_countryTo.setD_noOfArmies(l_toArmies);

                        l_fromArmies=l_fromArmies-d_noOfArmies;
                        l_countryfrom.setD_noOfArmies(l_fromArmies);
                        d_player.getD_ownedCountries().add(l_countryTo);
                        d_player.setD_isWinner(true);
                        l_targetPlayer.getD_ownedCountries().remove(l_countryToIndex);
                    }
                    else
                    {
                        l_toArmies=l_toArmies-l_attackArmiesFrom;
                        int l_remainingCountries;
                        if(d_noOfArmies>=l_attackArmiesTo) {
                            l_remainingCountries = d_noOfArmies - l_attackArmiesTo;
                        }
                        else
                        {
                            l_remainingCountries=0;
                        }
                        l_fromArmies=l_fromArmies-d_noOfArmies+l_remainingCountries;
                        l_countryfrom.setD_noOfArmies(l_fromArmies);
                        l_countryTo.setD_noOfArmies(l_toArmies);
                        d_player.getD_ownedCountries().remove(l_countryFromIndex);
                        d_player.getD_ownedCountries().add(l_countryFromIndex,l_countryfrom);
                        l_targetPlayer.getD_ownedCountries().remove(l_countryToIndex);
                        l_targetPlayer.getD_ownedCountries().add(l_countryToIndex,l_countryTo);
                    }



                    d_gameData.getD_playerList().remove(l_playerFromIndex);
                    d_gameData.getD_playerList().add(l_playerFromIndex,d_player);
                    d_gameData.getD_playerList().remove(l_playerToIndex);
                    d_gameData.getD_playerList().add(l_playerToIndex,l_targetPlayer);

                    if (d_gameData.getD_warMap().getD_continents() != null) {
                        for (Map.Entry<Integer, Continent> l_entry : d_gameData.getD_warMap().getD_continents().entrySet()) {
                            for (Country l_countries : l_entry.getValue().getD_countryList()) {
                                if (l_countries.getD_countryName().equalsIgnoreCase(d_CountryNameFrom)) {
                                    l_countries.setD_noOfArmies(l_fromArmies);                              //sets the no. of armies to the country of map
                                }
                                if (l_countries.getD_countryName().equalsIgnoreCase(d_CountryNameTo)) {
                                    l_countries.setD_noOfArmies(l_toArmies);                              //sets the no. of armies to the country of map
                                }
                            }
                        }
                    }
                    return true;
                }
            }

        } else {
            System.out.println("country from is not there");
            return false;
        }
        return false;

    }

    public Country getPlayerCountrybyName(String p_cName) {
        for (Player l_player : d_gameData.getD_playerList()) {
            for (Country l_country : l_player.getD_ownedCountries()) {
                if (l_country.getD_countryName().equalsIgnoreCase(p_cName)) {
                    return l_country;
                }
            }
        }
        return null;
    }

    public boolean isneighbourCountry(String p_countryName,Country p_countryFrom)
    {
        for(String l_country:p_countryFrom.getD_neighbourCountries())
        {
            if(l_country.equalsIgnoreCase(p_countryName))
            {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param p_countryFromName
     * @param p_countryNameTo
     * @param p_noOfArmies
     * @return
     */
    public boolean validateAndSetData(String p_countryFromName,String p_countryNameTo,int p_noOfArmies) {
        if (!p_countryFromName.isEmpty() && !p_countryNameTo.isEmpty()) {
            this.setD_CountryNameFrom(p_countryFromName);
            this.setD_CountryNameTo(p_countryNameTo);
            this.setD_noOfArmies(p_noOfArmies);
            return true;
        }
        return false;
    }
}
