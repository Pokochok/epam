<#macro page tour>
    <#import "security.ftl" as springSecurity/>
    <#import "/spring.ftl" as spring/>
    <#assign depDate = tour.getDepartureDate()! ['java.lang.Long']>
    <div class="tourItem">
        <div class="MinInf">
            <div class="TourName"
                <#if ((tour.status)!"") == "NOT_AVAILABLE"> style="color: red"</#if>>
                ${tour.tourName!}
            </div>


            <div class="arrivalPlace">
                ${tour.arrivalCountry!}, ${tour.arrivalCity!}
            </div>
        </div>

    <div class="departureCity">
        <form method="POST" action="to_tour_overview">
<#--            <input type="hidden" name="tourInstance" value="${tour!}"/>-->
            <input type="hidden" name="tourName" value="${tour.tourName!}"/>
            <input type="hidden" name="id" value="${tour.id?int!}"/>
            <input type="hidden" name="arrivalCountry" value="${tour.arrivalCountry!}"/>
            <input type="hidden" name="arrivalCity" value="${tour.arrivalCity!}"/>
            <input type="hidden" name="departureCity" value="${tour.departureCity!}"/>
            <input type="hidden" name="departureDate" value="${(tour.getDepartureDate()?long?c)!}"/>
            <input type="hidden" name="arrivalDate" value="${(tour.getArrivalDate()?long?c)!}"/>
            <input type="hidden" name="hotel" value="${tour.hotel!}"/>
            <input type="hidden" name="nutrition" value="${tour.nutrition!}"/>
            <input type="hidden" name="adultsNumber" value="${tour.adultsNumber!}"/>
            <input type="hidden" name="childrenNumber" value="${tour.childrenNumber!}"/>
            <input type="hidden" name="price" value="${tour.getPrice()?long?c!}"/>
            <input type="hidden" name="status" value="${tour.status!}"/>

            <div>
                <@spring.message "common.tourForm.submit.from"/>
                ${tour.departureCity!} <p>
            </div>
            <div class="submitMore">
                <label>
                    <input  class="uui-button transparent blue" type="submit" value="<@spring.message "common.tourForm.submit.more"/>">
                </label>
            </div>
        </form>

    </div>
    </div>
</#macro>