package services;

import com.povzner.continent.demo.entity.Travel;
import com.povzner.continent.demo.interfaces.ITravel;
import com.povzner.continent.demo.interfaces.StateConstants;
import com.povzner.continent.demo.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
//Закомментированный код являеться реализацией update state в состояние open из состояния in progress
@Service
public class TravelService implements ITravel {
    @Autowired
    TravelRepository repository;
   private ScheduledExecutorService executorService;
   // ConcurrentHashMap<Long, ScheduledFuture> updateStateTasks;

    public TravelService(TravelRepository travelRepository) {
        this.repository = travelRepository;
        this.executorService = Executors.newScheduledThreadPool(1);
        //this.updateStateTasks = new ConcurrentHashMap<>();
    }

    @Override

    public ResponseEntity<Long> createTravel(Travel travel) {
        if(checkState(travel.getState())==false){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        repository.save(travel);
        if (travel.getState().equals(StateConstants.IN_PROGRESS)) {
            useScheldueExecutor(travel);
        }
        return new ResponseEntity<>(travel.getId(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Travel> updateTravel(Travel travel, Long id) {
        if(checkState(travel.getState())==false){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
       /* if(travel.getState().equals(StateConstants.OPEN)) {
            setOpenState(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }*/
        if (repository.findById(id).isPresent()) {
            travel.setId(id);
            repository.save(travel);
            if (travel.getState().equals(StateConstants.IN_PROGRESS)) {
                useScheldueExecutor(travel);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Travel> getTravelById(Long id) {
        if (repository.findById(id).isPresent()) {
            return new ResponseEntity<>(repository.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    private void useScheldueExecutor(Travel travel) {
        ScheduledFuture<?> scheduledFuture = executorService.schedule(()
                -> changeState(travel), 1, TimeUnit.MINUTES);
       // updateStateTasks.put(travel.getId(), scheduledFuture);

    }

    private void changeState(Travel travel) {
        travel.setState(StateConstants.CLOSED);
        repository.save(travel);
        System.out.println("DONE");
    }

    private boolean checkState(String state) {
        if (!state.equals(StateConstants.OPEN) && !state.equals(StateConstants.IN_PROGRESS)) {
            return false;
        }
        return true;
    }

   /* private void setOpenState(Long id) {
        System.out.println(updateStateTasks.get(id));
        if (updateStateTasks.get(id) == null) {
            System.out.println("ScheduledFuture with " + id + " is not found");
            return;
        }
        Travel travel = repository.findById(id).get();
        travel.setState(StateConstants.OPEN);
        repository.save(travel);
        updateStateTasks.get(id).cancel(false);
        updateStateTasks.remove(id);
    }*/
}

